package me.dgahn.vms.ui

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

private const val DOCS_NAME = "index"

@Controller
@RequestMapping("/api/docs")
class DocController {
    @GetMapping
    fun docs(): String = DOCS_NAME
}
