package com.resumebuilder.dto;

import lombok.Data;
import java.util.List;

@Data
public class ParsedResumeData {
    private String name;
    private String phone;
    private String email;
    private String city;
    private String intendedPosition;
    private String website;
    private String github;
    private String linkedin;
    private List<ParsedEducation> educationList;
    private List<ParsedWork> workExperienceList;
    private List<String> skillNames;
    private List<ParsedCertificate> certificateList;
    private String selfEvaluationContent;

    @Data
    public static class ParsedEducation {
        private String school;
        private String major;
        private String degree;
        private String gpa;
        private String startDate;
        private String endDate;
        private String description;
    }

    @Data
    public static class ParsedWork {
        private String company;
        private String position;
        private String startDate;
        private String endDate;
        private String description;
    }

    @Data
    public static class ParsedCertificate {
        private String name;
        private String issuingAuthority;
        private String obtainDate;
    }
}
