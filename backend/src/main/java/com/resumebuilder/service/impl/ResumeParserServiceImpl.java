package com.resumebuilder.service.impl;

import cn.hutool.core.util.ReUtil;
import com.resumebuilder.dto.ParsedResumeData;
import com.resumebuilder.entity.*;
import com.resumebuilder.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ResumeParserServiceImpl implements ResumeParserService {

    private final BasicInfoService basicInfoService;
    private final EducationService educationService;
    private final WorkExperienceService workExperienceService;
    private final ProjectExperienceService projectExperienceService;
    private final SkillTagService skillTagService;
    private final CertificateService certificateService;
    private final SelfEvaluationService selfEvaluationService;

    private static final Pattern PHONE_PATTERN = Pattern.compile("1[3-9]\\d{9}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern DATE_RANGE_PATTERN = Pattern.compile("(\\d{4}[\\.\\-/年]\\d{1,2}[\\.\\-/月]?)\\s*[-~至到]\\s*(\\d{4}[\\.\\-/年]\\d{1,2}[\\.\\-/月]?|至今|现在|Now)");

    @Override
    public ParsedResumeData parse(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            Tika tika = new Tika();
            String content = tika.parseToString(fis);
            return parseContent(content);
        } catch (Exception e) {
            throw new RuntimeException("文件解析失败: " + e.getMessage(), e);
        }
    }

    private ParsedResumeData parseContent(String text) {
        ParsedResumeData result = new ParsedResumeData();

        // Extract phone
        result.setPhone(ReUtil.getGroup0(PHONE_PATTERN, text));
        // Extract email
        result.setEmail(ReUtil.getGroup0(EMAIL_PATTERN, text));

        // Extract name: first non-empty line with 2-4 Chinese chars, near the top
        String[] lines = text.split("\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;
            String chineseName = ReUtil.getGroup0("^[\\u4e00-\\u9fa5]{2,4}$", trimmed);
            if (chineseName != null) {
                result.setName(chineseName);
                break;
            }
        }

        // Split text into sections by common resume section headers
        List<String> sections = splitSections(text);

        for (String section : sections) {
            String lower = section.toLowerCase();
            if (containsAny(lower, "教育", "education", "学历", "学习")) {
                result.setEducationList(parseEducationSection(section));
            } else if (containsAny(lower, "工作", "work", "实习", "经历", "experience", "employment")) {
                result.setWorkExperienceList(parseWorkSection(section));
            } else if (containsAny(lower, "技能", "skill", "技术", "tech")) {
                result.setSkillNames(parseSkillsSection(section));
            } else if (containsAny(lower, "证书", "certificate", "获奖", "award")) {
                result.setCertificateList(parseCertificateSection(section));
            } else if (containsAny(lower, "自我评价", "评价", "个人", "self", "summary", "profile")) {
                result.setSelfEvaluationContent(section.replaceAll("(?i)(自我评价|评价|self.evaluation|个人简介|profile|summary)[:：]?", "").trim());
            }
        }

        return result;
    }

    private List<String> splitSections(String text) {
        // Common Chinese and English section headers
        String[] headers = {
                "教育经历", "教育背景", "学历", "Education",
                "工作经历", "工作经验", "实习经历", "Work Experience", "Employment",
                "项目经历", "项目经验", "Project Experience", "Projects",
                "技能", "专业技能", "技术栈", "Skills", "Technical Skills",
                "证书", "获奖", "证书奖项", "Certificates", "Awards",
                "自我评价", "个人评价", "自我介绍", "Self Evaluation", "Summary", "Profile",
        };

        // Find the positions of each header
        Map<Integer, String> positions = new TreeMap<>();
        for (String header : headers) {
            int idx = text.indexOf(header);
            if (idx >= 0) {
                positions.put(idx, header);
            }
            // Try with colon
            idx = text.indexOf(header + "：");
            if (idx >= 0) positions.put(idx, header);
            idx = text.indexOf(header + ":");
            if (idx >= 0) positions.put(idx, header);
        }

        if (positions.isEmpty()) {
            return Collections.singletonList(text);
        }

        List<String> result = new ArrayList<>();
        List<Integer> posList = new ArrayList<>(positions.keySet());
        for (int i = 0; i < posList.size(); i++) {
            int start = posList.get(i);
            int end = (i + 1 < posList.size()) ? posList.get(i + 1) : text.length();
            result.add(text.substring(start, end).trim());
        }
        return result;
    }

    private List<ParsedResumeData.ParsedEducation> parseEducationSection(String section) {
        List<ParsedResumeData.ParsedEducation> list = new ArrayList<>();
        // Split by blank lines or bullet points
        String[] blocks = section.split("\\n\\s*\\n|•|●|·|- ");
        for (String block : blocks) {
            String trimmed = block.trim();
            if (trimmed.length() < 5) continue;
            // Skip the header line
            if (containsAny(trimmed, "教育", "Education")) continue;

            ParsedResumeData.ParsedEducation edu = new ParsedResumeData.ParsedEducation();
            // Try to extract school name (usually first line or contains "大学"/"学院")
            String[] subLines = trimmed.split("\\n");
            for (String line : subLines) {
                String l = line.trim();
                if (l.isEmpty()) continue;
                if (edu.getSchool() == null && (l.contains("大学") || l.contains("学院") || l.contains("University"))) {
                    edu.setSchool(l);
                } else if (edu.getMajor() == null && l.length() < 30) {
                    // Could be major/degree line
                    if (l.contains("专业") || l.contains("Major")) {
                        edu.setMajor(l);
                    } else if (edu.getSchool() != null) {
                        edu.setMajor(l);
                    }
                }
                // Extract dates
                String dates = ReUtil.getGroup0(DATE_RANGE_PATTERN, l);
                if (dates != null && dates.length() > 5) {
                    String[] parts = dates.split("\\s*[-~至到]\\s*");
                    if (parts.length >= 2) {
                        edu.setStartDate(parts[0].trim());
                        edu.setEndDate(parts[1].trim());
                    }
                }
            }
            if (edu.getSchool() != null) list.add(edu);
        }
        return list;
    }

    private List<ParsedResumeData.ParsedWork> parseWorkSection(String section) {
        List<ParsedResumeData.ParsedWork> list = new ArrayList<>();
        String[] blocks = section.split("\\n\\s*\\n|•|●|·|- ");
        for (String block : blocks) {
            String trimmed = block.trim();
            if (trimmed.length() < 5) continue;
            if (containsAny(trimmed.toLowerCase(), "工作", "work", "实习", "经历", "experience", "employment") && trimmed.length() < 20) continue;

            ParsedResumeData.ParsedWork work = new ParsedResumeData.ParsedWork();
            String[] subLines = trimmed.split("\\n");
            for (String line : subLines) {
                String l = line.trim();
                if (l.isEmpty()) continue;
                if (work.getCompany() == null) {
                    work.setCompany(l);
                } else if (work.getPosition() == null && l.length() < 40) {
                    work.setPosition(l);
                }
                String dates = ReUtil.getGroup0(DATE_RANGE_PATTERN, l);
                if (dates != null && dates.length() > 5) {
                    String[] parts = dates.split("\\s*[-~至到]\\s*");
                    if (parts.length >= 2) {
                        work.setStartDate(parts[0].trim());
                        work.setEndDate(parts[1].trim());
                    }
                }
            }
            if (work.getCompany() != null) list.add(work);
        }
        return list;
    }

    private List<String> parseSkillsSection(String section) {
        List<String> skills = new ArrayList<>();
        // Remove header line
        String content = section.replaceFirst("(?i)(技能|专业技能|技术栈|Skills|Technical.Skills)[:：]?", "");
        // Split by common delimiters
        String[] parts = content.split("[,，、/\\|·•\\n]");
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.length() >= 2 && trimmed.length() <= 30 && !trimmed.matches(".*[a-z]{3,}.*")) {
                skills.add(trimmed);
            } else if (trimmed.length() >= 2 && trimmed.length() <= 20) {
                skills.add(trimmed);
            }
        }
        return skills;
    }

    private List<ParsedResumeData.ParsedCertificate> parseCertificateSection(String section) {
        List<ParsedResumeData.ParsedCertificate> list = new ArrayList<>();
        String[] lines = section.split("\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.length() < 3) continue;
            if (containsAny(trimmed, "证书", "Certificates", "获奖", "Awards")) continue;

            ParsedResumeData.ParsedCertificate cert = new ParsedResumeData.ParsedCertificate();
            cert.setName(trimmed);
            list.add(cert);
        }
        return list;
    }

    private boolean containsAny(String text, String... keywords) {
        for (String kw : keywords) {
            if (text.contains(kw)) return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void importData(Long userId, ParsedResumeData data) {
        if (data == null) return;

        // BasicInfo
        BasicInfo info = basicInfoService.getByUserId(userId);
        if (info == null) {
            info = new BasicInfo();
            info.setUserId(userId);
        }
        if (data.getName() != null) info.setName(data.getName());
        if (data.getPhone() != null) info.setPhone(data.getPhone());
        if (data.getEmail() != null) info.setEmail(data.getEmail());
        if (data.getCity() != null) info.setCity(data.getCity());
        if (data.getIntendedPosition() != null) info.setIntendedPosition(data.getIntendedPosition());
        if (data.getWebsite() != null) info.setWebsite(data.getWebsite());
        if (data.getGithub() != null) info.setGithub(data.getGithub());
        if (data.getLinkedin() != null) info.setLinkedin(data.getLinkedin());
        basicInfoService.saveOrUpdate(info);

        // Education
        if (data.getEducationList() != null) {
            for (ParsedResumeData.ParsedEducation e : data.getEducationList()) {
                Education edu = new Education();
                edu.setUserId(userId);
                edu.setSchool(e.getSchool() != null ? e.getSchool() : "");
                edu.setMajor(e.getMajor());
                edu.setDegree(e.getDegree());
                edu.setGpa(e.getGpa());
                edu.setDescription(e.getDescription());
                educationService.save(edu);
            }
        }

        // Work
        if (data.getWorkExperienceList() != null) {
            for (ParsedResumeData.ParsedWork w : data.getWorkExperienceList()) {
                WorkExperience work = new WorkExperience();
                work.setUserId(userId);
                work.setCompany(w.getCompany() != null ? w.getCompany() : "");
                work.setPosition(w.getPosition());
                work.setDescription(w.getDescription());
                workExperienceService.save(work);
            }
        }

        // Skills
        if (data.getSkillNames() != null) {
            for (String skillName : data.getSkillNames()) {
                SkillTag tag = new SkillTag();
                tag.setUserId(userId);
                tag.setSkillName(skillName);
                tag.setProficiency(3);
                skillTagService.save(tag);
            }
        }

        // Certificates
        if (data.getCertificateList() != null) {
            for (ParsedResumeData.ParsedCertificate c : data.getCertificateList()) {
                Certificate cert = new Certificate();
                cert.setUserId(userId);
                cert.setName(c.getName() != null ? c.getName() : "");
                cert.setIssuingAuthority(c.getIssuingAuthority());
                certificateService.save(cert);
            }
        }

        // SelfEvaluation
        if (data.getSelfEvaluationContent() != null && !data.getSelfEvaluationContent().isEmpty()) {
            SelfEvaluation eval = selfEvaluationService.getByUserId(userId);
            if (eval == null) {
                eval = new SelfEvaluation();
                eval.setUserId(userId);
            }
            eval.setContent(data.getSelfEvaluationContent());
            selfEvaluationService.saveOrUpdate(eval);
        }
    }
}
