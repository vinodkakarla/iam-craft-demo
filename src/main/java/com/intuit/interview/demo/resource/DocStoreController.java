package com.intuit.interview.demo.resource;

import com.intuit.interview.demo.service.DocStoreServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * This class is a REST controller that handles requests related to the document store.
 * It uses the DocStoreServiceImpl to interact with the document store.
 */
@RestController
@RequestMapping("/api/v1/doc-store")
public class DocStoreController {

    // The DocStoreServiceImpl used to interact with the document store
    private final DocStoreServiceImpl docStoreService;

    /**
     * This constructor initializes the DocStoreController with the given DocStoreServiceImpl.
     *
     * @param docStoreService the DocStoreServiceImpl to use
     */
    public DocStoreController(DocStoreServiceImpl docStoreService) {
        this.docStoreService = docStoreService;
    }

    /**
     * This method handles GET requests to list all objects in the document store.
     *
     * @return a list of all objects in the document store
     */
    @GetMapping("/print-all")
    public List<String> listObjects() {
        return docStoreService.listObjects();
    }

    /**
     * This method handles GET requests to retrieve the metadata of a specific object.
     * The object is specified by the path after "/object_metadata/" in the request URL.
     *
     * @param request the HttpServletRequest containing the request URL
     * @return the metadata of the specified object
     */
    @GetMapping("/object_metadata/**")
    public String getObjectMetadata(HttpServletRequest request) {
        return docStoreService.getObjectMetadata(decode(request));
    }

    /**
     * This method handles GET requests to retrieve the audit log of object metadata.
     *
     * @return a list of the last few lines of the audit log
     */
    @GetMapping("/audit_log")
    public List<String> auditObjectMetadata() {
        return docStoreService.auditObjectMetadata();
    }

    /**
     * This method decodes the object name from the request URL.
     * It is used to retrieve the object name from the path after "/object_metadata/" in the request URL.
     *
     * @param request the HttpServletRequest containing the request URL
     * @return the decoded object name
     */
    private String decode(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        String fileName = requestURL.split("/object_metadata/")[1];
        return java.net.URLDecoder.decode(fileName, StandardCharsets.UTF_8);
    }
}