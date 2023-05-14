package com.sequitur.api.ProactiveCommunication.domain.service;

import com.sequitur.api.ProactiveCommunication.domain.model.Support;

import java.util.List;

public interface SupportService {
    List<Support> getAllSupport();
    Support createSupport(Support support);
}
