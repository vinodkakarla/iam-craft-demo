package com.intuit.interview.demo.resource;

import com.intuit.interview.demo.service.DocStoreServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

/**
 * This class is a REST controller that handles requests related to the document store.
 * It uses the DocStoreServiceImpl to interact with the document store.
 */
@RestController
@RequestMapping("/api/v1/doc-store")
public class DocStoreResourceImpl {

    // The DocStoreServiceImpl used to interact with the document store
    private final DocStoreServiceImpl docStoreService;

    /**
     * This constructor initializes the DocStoreController with the given DocStoreServiceImpl.
     *
     * @param docStoreService the DocStoreServiceImpl to use
     */
    public DocStoreResourceImpl(DocStoreServiceImpl docStoreService) {
        this.docStoreService = docStoreService;
    }

    /**
     * This method handles GET requests to list all objects in the document store.
     *
     * @return a list of all objects in the document store
     */
    @GetMapping("/print-all")
    @PreAuthorize("hasAuthority('SCOPE_email')")
    public List<String> listObjects(@AuthenticationPrincipal OidcUser user, @RequestParam(name = "bucket") String bucket) {
        return docStoreService.listObjects(bucket);
    }

    /**
     * This method handles GET requests to retrieve the metadata of a specific object.
     * The object is specified by the path after "/object_metadata/" in the request URL.
     *
     * @param request the HttpServletRequest containing the request URL
     * @return the metadata of the specified object
     */
    @GetMapping("/object_metadata/**")
    public String getObjectMetadata(Principal principal, HttpServletRequest request) {
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