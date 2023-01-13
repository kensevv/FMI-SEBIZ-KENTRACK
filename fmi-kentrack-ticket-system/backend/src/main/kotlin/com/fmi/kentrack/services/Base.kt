package com.fmi.kentrack.services

import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Base {
    @Autowired
    protected lateinit var db: DSLContext
}
