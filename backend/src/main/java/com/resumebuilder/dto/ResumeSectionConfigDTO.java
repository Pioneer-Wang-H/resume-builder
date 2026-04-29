package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ResumeSectionConfigDTO {
    @NotBlank
    private String sectionType;

    private Boolean enabled;

    private Integer sortOrder;

    private List<Long> itemIds;
}
