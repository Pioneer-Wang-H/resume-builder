package com.resumebuilder.service;

import com.resumebuilder.entity.Certificate;

import java.util.List;

public interface CertificateService {
    List<Certificate> getByUserId(Long userId);
    void save(Certificate certificate);
    void update(Certificate certificate);
    void delete(Long id);
}
