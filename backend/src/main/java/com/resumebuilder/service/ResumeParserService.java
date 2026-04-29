package com.resumebuilder.service;

import com.resumebuilder.dto.ParsedResumeData;

import java.io.File;

public interface ResumeParserService {
    ParsedResumeData parse(File file);
    void importData(Long userId, ParsedResumeData data);
}
