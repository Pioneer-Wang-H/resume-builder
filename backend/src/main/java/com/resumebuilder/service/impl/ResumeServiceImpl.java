package com.resumebuilder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.dto.ResumeCreateDTO;
import com.resumebuilder.dto.ResumeSectionConfigDTO;
import com.resumebuilder.entity.*;
import com.resumebuilder.mapper.*;
import com.resumebuilder.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;
    private final ResumeSectionMapper resumeSectionMapper;
    private final ResumeSectionItemMapper resumeSectionItemMapper;
    private final ResumeTemplateMapper resumeTemplateMapper;
    private final BasicInfoService basicInfoService;
    private final EducationService educationService;
    private final ProjectExperienceService projectExperienceService;
    private final WorkExperienceService workExperienceService;
    private final SkillTagService skillTagService;
    private final CertificateService certificateService;
    private final SelfEvaluationService selfEvaluationService;

    @Override
    @Transactional
    public Resume create(Long userId, ResumeCreateDTO dto) {
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle(dto.getTitle());
        resume.setTemplateId(dto.getTemplateId() != null ? dto.getTemplateId() : 1L);
        resumeMapper.insert(resume);

        String[] sections = {"basic_info", "education", "project_experience", "work_experience", "skills", "certificates", "self_evaluation"};
        for (int i = 0; i < sections.length; i++) {
            ResumeSection section = new ResumeSection();
            section.setResumeId(resume.getId());
            section.setSectionType(sections[i]);
            section.setEnabled(1);
            section.setSortOrder(i);
            resumeSectionMapper.insert(section);
        }
        return resume;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        resumeSectionItemMapper.delete(new LambdaQueryWrapper<ResumeSectionItem>()
                .in(ResumeSectionItem::getSectionId,
                        resumeSectionMapper.selectList(new LambdaQueryWrapper<ResumeSection>()
                                        .eq(ResumeSection::getResumeId, id)).stream()
                                .map(ResumeSection::getId).collect(Collectors.toList())));
        resumeSectionMapper.delete(new LambdaQueryWrapper<ResumeSection>()
                .eq(ResumeSection::getResumeId, id));
        resumeMapper.deleteById(id);
    }

    @Override
    public Resume getById(Long id) {
        return resumeMapper.selectById(id);
    }

    @Override
    public List<Resume> getByUserId(Long userId) {
        return resumeMapper.selectList(new LambdaQueryWrapper<Resume>()
                .eq(Resume::getUserId, userId)
                .orderByDesc(Resume::getCreateTime));
    }

    @Override
    public void updateTitle(Long id, String title) {
        Resume resume = resumeMapper.selectById(id);
        if (resume != null) {
            resume.setTitle(title);
            resumeMapper.updateById(resume);
        }
    }

    @Override
    public void setDefault(Long userId, Long resumeId) {
        resumeMapper.selectList(new LambdaQueryWrapper<Resume>()
                .eq(Resume::getUserId, userId)).forEach(r -> {
            r.setIsDefault(r.getId().equals(resumeId) ? 1 : 0);
            resumeMapper.updateById(r);
        });
    }

    @Override
    @Transactional
    @CacheEvict(value = "resumeAssemble", key = "#resumeId")
    public void configureSections(Long resumeId, List<ResumeSectionConfigDTO> configs) {
        for (ResumeSectionConfigDTO config : configs) {
            ResumeSection section = resumeSectionMapper.selectOne(new LambdaQueryWrapper<ResumeSection>()
                    .eq(ResumeSection::getResumeId, resumeId)
                    .eq(ResumeSection::getSectionType, config.getSectionType()));
            if (section == null) continue;

            section.setEnabled(config.getEnabled() != null ? (config.getEnabled() ? 1 : 0) : 1);
            if (config.getSortOrder() != null) {
                section.setSortOrder(config.getSortOrder());
            }
            resumeSectionMapper.updateById(section);

            // 更新素材勾选
            if (config.getItemIds() != null) {
                resumeSectionItemMapper.delete(new LambdaQueryWrapper<ResumeSectionItem>()
                        .eq(ResumeSectionItem::getSectionId, section.getId()));
                for (int i = 0; i < config.getItemIds().size(); i++) {
                    ResumeSectionItem item = new ResumeSectionItem();
                    item.setSectionId(section.getId());
                    item.setItemType(config.getSectionType());
                    item.setItemId(config.getItemIds().get(i));
                    item.setSortOrder(i);
                    resumeSectionItemMapper.insert(item);
                }
            }
        }
    }

    @Override
    @Cacheable(value = "resumeAssemble", key = "#resumeId")
    public Map<String, Object> assemble(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) throw new RuntimeException("简历不存在");

        Long userId = resume.getUserId();
        ResumeTemplate template = resumeTemplateMapper.selectById(resume.getTemplateId());

        // 返回所有 sections（含禁用的），供前端编辑用
        List<ResumeSection> allSections = resumeSectionMapper.selectList(
                new LambdaQueryWrapper<ResumeSection>()
                        .eq(ResumeSection::getResumeId, resumeId)
                        .orderByAsc(ResumeSection::getSortOrder));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resume", resume);
        result.put("template", template);

        BasicInfo basicInfo = basicInfoService.getByUserId(userId);
        result.put("basicInfo", basicInfo);

        // 返回 sections 配置（含选中项 ID）
        List<Map<String, Object>> sectionConfigs = new ArrayList<>();
        for (ResumeSection section : allSections) {
            List<ResumeSectionItem> items = resumeSectionItemMapper.selectList(
                    new LambdaQueryWrapper<ResumeSectionItem>()
                            .eq(ResumeSectionItem::getSectionId, section.getId())
                            .orderByAsc(ResumeSectionItem::getSortOrder));

            Set<Long> selectedIds = items.stream().map(ResumeSectionItem::getItemId).collect(Collectors.toSet());

            Map<String, Object> sc = new LinkedHashMap<>();
            sc.put("id", section.getId());
            sc.put("sectionType", section.getSectionType());
            sc.put("enabled", section.getEnabled() == 1);
            sc.put("sortOrder", section.getSortOrder());
            sc.put("itemIds", new ArrayList<>(selectedIds));
            sectionConfigs.add(sc);

            // 只有启用的模块才加载数据到预览
            if (section.getEnabled() == 1) {
                switch (section.getSectionType()) {
                    case "education" -> {
                        List<Education> all = educationService.getByUserId(userId);
                        result.put("education", selectedIds.isEmpty() ? all :
                                all.stream().filter(e -> selectedIds.contains(e.getId())).collect(Collectors.toList()));
                    }
                    case "project_experience" -> {
                        List<ProjectExperience> all = projectExperienceService.getByUserId(userId);
                        result.put("projectExperience", selectedIds.isEmpty() ? all :
                                all.stream().filter(e -> selectedIds.contains(e.getId())).collect(Collectors.toList()));
                    }
                    case "work_experience" -> {
                        List<WorkExperience> all = workExperienceService.getByUserId(userId);
                        result.put("workExperience", selectedIds.isEmpty() ? all :
                                all.stream().filter(e -> selectedIds.contains(e.getId())).collect(Collectors.toList()));
                    }
                    case "skills" -> result.put("skills", skillTagService.getByUserId(userId));
                    case "certificates" -> {
                        List<Certificate> all = certificateService.getByUserId(userId);
                        result.put("certificates", selectedIds.isEmpty() ? all :
                                all.stream().filter(e -> selectedIds.contains(e.getId())).collect(Collectors.toList()));
                    }
                    case "self_evaluation" -> result.put("selfEvaluation", selfEvaluationService.getByUserId(userId));
                }
            }
        }
        result.put("sections", sectionConfigs);

        return result;
    }

    @Override
    public byte[] exportPdf(Long resumeId) {
        Map<String, Object> data = assemble(resumeId);
        BasicInfo basicInfo = (BasicInfo) data.get("basicInfo");
        String html = buildResumeHtml(data);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            registerChineseFont(renderer);
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(os);
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF生成失败: " + e.getMessage(), e);
        }
    }

    private String buildResumeHtml(Map<String, Object> data) {
        BasicInfo info = (BasicInfo) data.get("basicInfo");
        @SuppressWarnings("unchecked")
        List<Education> eduList = (List<Education>) data.get("education");
        @SuppressWarnings("unchecked")
        List<WorkExperience> workList = (List<WorkExperience>) data.get("workExperience");
        @SuppressWarnings("unchecked")
        List<ProjectExperience> projList = (List<ProjectExperience>) data.get("projectExperience");
        @SuppressWarnings("unchecked")
        List<SkillTag> skillList = (List<SkillTag>) data.get("skills");
        @SuppressWarnings("unchecked")
        List<Certificate> certList = (List<Certificate>) data.get("certificates");
        SelfEvaluation eval = (SelfEvaluation) data.get("selfEvaluation");

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><style>");
        sb.append("*{margin:0;padding:0;box-sizing:border-box;}");
        sb.append("body{font-family:'Microsoft YaHei','SimHei','SimSun',sans-serif;font-size:13px;color:#1a202c;}");
        sb.append(".paper{width:210mm;min-height:297mm;display:flex;}");
        sb.append(".sidebar{width:200px;background:#1a365d;color:#e2e8f0;padding:28px 18px;}");
        sb.append(".main{flex:1;padding:28px 36px;}");
        sb.append(".avatar{width:72px;height:72px;border-radius:50%;background:#4299e1;color:#fff;");
        sb.append("display:flex;align-items:center;justify-content:center;font-size:28px;margin:0 auto 16px;}");
        sb.append(".contact{font-size:11px;line-height:1.8;margin-bottom:20px;}");
        sb.append(".sidebar h3{font-size:14px;border-bottom:1px solid rgba(255,255,255,0.2);padding-bottom:4px;margin:16px 0 10px;}");
        sb.append(".skill-item{background:rgba(255,255,255,0.12);display:inline-block;padding:2px 10px;");
        sb.append("border-radius:10px;font-size:11px;margin:2px 4px 4px 0;}");
        sb.append(".main h1{font-size:26px;margin-bottom:2px;}");
        sb.append(".main .position{color:#4a5568;font-size:15px;margin-bottom:20px;}");
        sb.append(".main h3{font-size:15px;color:#1a365d;border-bottom:2px solid #1a365d;padding-bottom:3px;margin:18px 0 10px;}");
        sb.append(".item{margin-bottom:10px;}");
        sb.append(".item-header{display:flex;justify-content:space-between;font-size:13px;}");
        sb.append(".item-sub{font-size:12px;color:#4a5568;}");
        sb.append(".item p{font-size:12px;color:#4a5568;line-height:1.5;margin-top:2px;}");
        sb.append("</style></head><body><div class=\"paper\">");

        // Sidebar
        sb.append("<div class=\"sidebar\">");
        String name = info != null && info.getName() != null ? info.getName() : "未填写";
        sb.append("<div class=\"avatar\">").append(name.isEmpty() ? "?" : name.charAt(0)).append("</div>");
        sb.append("<div class=\"contact\">");
        if (info != null) {
            if (info.getPhone() != null) sb.append("<div>").append(escapeHtml(info.getPhone())).append("</div>");
            if (info.getEmail() != null) sb.append("<div>").append(escapeHtml(info.getEmail())).append("</div>");
            if (info.getCity() != null) sb.append("<div>").append(escapeHtml(info.getCity())).append("</div>");
            if (info.getGithub() != null) sb.append("<div>").append(escapeHtml(info.getGithub())).append("</div>");
            if (info.getLinkedin() != null) sb.append("<div>").append(escapeHtml(info.getLinkedin())).append("</div>");
        }
        sb.append("</div>");

        if (skillList != null && !skillList.isEmpty()) {
            sb.append("<h3>技能</h3><div>");
            for (SkillTag s : skillList) {
                sb.append("<span class=\"skill-item\">").append(escapeHtml(s.getSkillName())).append("</span>");
            }
            sb.append("</div>");
        }
        sb.append("</div>");

        // Main content
        sb.append("<div class=\"main\">");
        sb.append("<h1>").append(escapeHtml(name)).append("</h1>");
        if (info != null && info.getIntendedPosition() != null) {
            sb.append("<div class=\"position\">").append(escapeHtml(info.getIntendedPosition())).append("</div>");
        } else {
            sb.append("<div class=\"position\">&nbsp;</div>");
        }

        if (eduList != null && !eduList.isEmpty()) {
            sb.append("<h3>教育经历</h3>");
            for (Education e : eduList) {
                sb.append("<div class=\"item\"><div class=\"item-header\"><strong>").append(escapeHtml(e.getSchool())).append("</strong>");
                sb.append("<span>").append(fmt(e.getStartDate())).append(" ~ ").append(fmt(e.getEndDate())).append("</span></div>");
                sb.append("<div class=\"item-sub\">").append(escapeHtml(nullToEmpty(e.getMajor())));
                sb.append(" · ").append(escapeHtml(nullToEmpty(e.getDegree())));
                if (e.getGpa() != null && !e.getGpa().isEmpty()) sb.append(" · GPA ").append(e.getGpa());
                sb.append("</div>");
                if (e.getDescription() != null) sb.append("<p>").append(escapeHtml(e.getDescription())).append("</p>");
                sb.append("</div>");
            }
        }

        if (workList != null && !workList.isEmpty()) {
            sb.append("<h3>工作经历</h3>");
            for (WorkExperience w : workList) {
                sb.append("<div class=\"item\"><div class=\"item-header\"><strong>").append(escapeHtml(w.getCompany())).append("</strong>");
                sb.append("<span>").append(fmt(w.getStartDate())).append(" ~ ").append(fmt(w.getEndDate())).append("</span></div>");
                sb.append("<div class=\"item-sub\">").append(escapeHtml(nullToEmpty(w.getPosition()))).append("</div>");
                if (w.getDescription() != null) sb.append("<p>").append(escapeHtml(w.getDescription())).append("</p>");
                sb.append("</div>");
            }
        }

        if (projList != null && !projList.isEmpty()) {
            sb.append("<h3>项目经历</h3>");
            for (ProjectExperience p : projList) {
                sb.append("<div class=\"item\"><div class=\"item-header\"><strong>").append(escapeHtml(p.getProjectName())).append("</strong>");
                sb.append("<span>").append(fmt(p.getStartDate())).append(" ~ ").append(fmt(p.getEndDate())).append("</span></div>");
                sb.append("<div class=\"item-sub\">").append(escapeHtml(nullToEmpty(p.getRole()))).append("</div>");
                if (p.getDescription() != null) sb.append("<p>").append(escapeHtml(p.getDescription())).append("</p>");
                sb.append("</div>");
            }
        }

        if (certList != null && !certList.isEmpty()) {
            sb.append("<h3>证书奖项</h3>");
            for (Certificate c : certList) {
                sb.append("<div class=\"item\"><strong>").append(escapeHtml(c.getName())).append("</strong>");
                if (c.getIssuingAuthority() != null) sb.append(" · ").append(escapeHtml(c.getIssuingAuthority()));
                if (c.getObtainDate() != null) sb.append(" · ").append(fmt(c.getObtainDate()));
                sb.append("</div>");
            }
        }

        if (eval != null && eval.getContent() != null && !eval.getContent().isEmpty()) {
            sb.append("<h3>自我评价</h3><p>").append(escapeHtml(eval.getContent())).append("</p>");
        }

        sb.append("</div></div></body></html>");
        return sb.toString();
    }

    private void registerChineseFont(ITextRenderer renderer) {
        String[] fontPaths = {
                "C:/Windows/Fonts/msyh.ttc",
                "C:/Windows/Fonts/simsun.ttc",
                "C:/Windows/Fonts/simhei.ttf",
                "/usr/share/fonts/truetype/noto/NotoSansCJK-Regular.ttc",
                "/System/Library/Fonts/PingFang.ttc",
        };
        for (String path : fontPaths) {
            try {
                File fontFile = new File(path);
                if (fontFile.exists()) {
                    renderer.getFontResolver().addFont(path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    return;
                }
            } catch (Exception ignored) {}
        }
        // fallback: try classpath font
        try {
            String classpathFont = getClass().getClassLoader().getResource("fonts/NotoSansSC-Regular.ttf").getPath();
            renderer.getFontResolver().addFont(classpathFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception ignored) {}
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String fmt(Object d) {
        return d != null ? d.toString() : "";
    }

    private String nullToEmpty(String s) {
        return s != null ? s : "";
    }
}
