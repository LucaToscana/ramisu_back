package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.repository.LineOfOrderRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LineOfOrderServiceImplement {
    private LineOfOrderRepository lineOfOrderRepository;
}
