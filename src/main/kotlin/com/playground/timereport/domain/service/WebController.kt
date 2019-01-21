package com.playground.timereport.domain.service

import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.Charset
import javax.servlet.http.HttpServletResponse

@RestController
class WebController {

    @RequestMapping(value = "/")
    fun indexPage(response: HttpServletResponse) {
        response.contentType = "text/html; charset=UTF-8"
        val template = StreamUtils.copyToString(ClassPathResource("index.html").inputStream, Charset.defaultCharset())
        response.outputStream.write(template.toByteArray(Charset.defaultCharset()))
    }
}