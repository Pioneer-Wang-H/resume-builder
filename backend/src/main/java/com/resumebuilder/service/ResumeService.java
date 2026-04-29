package com.resumebuilder.service;

import com.resumebuilder.dto.ResumeCreateDTO;
import com.resumebuilder.dto.ResumeSectionConfigDTO;
import com.resumebuilder.entity.Resume;

import java.util.List;
import java.util.Map;

public interface ResumeService {
    Resume create(Long userId, ResumeCreateDTO dto);
    void delete(Long id);
    Resume getById(Long id);
    List<Resume> getByUserId(Long userId);
    void updateTitle(Long id, String title);
    void setDefault(Long userId, Long resumeId);
    void configureSections(Long resumeId, List<ResumeSectionConfigDTO> sections);
    Map<String, Object> assemble(Long resumeId);
    byte[] exportPdf(Long resumeId);
}
