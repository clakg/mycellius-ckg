package fr.mycellius.web.controller;

import fr.mycellius.domain.WikiPage;
import fr.mycellius.service.WikiService;
import fr.mycellius.service.RevisionService;
import fr.mycellius.service.AuditService;
import fr.mycellius.web.dto.CreateWikiPageRequest;
import fr.mycellius.web.dto.WikiPageResponse;
import fr.mycellius.web.mapper.WikiPageDtoMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pages")
public class WikiPageController {

    private final WikiService wikiService;
    private final WikiPageDtoMapper mapper;
    private final RevisionService revisionService;
    private final AuditService auditService;

    public WikiPageController(WikiService wikiService,
                              WikiPageDtoMapper mapper,
                              RevisionService revisionService,
                              AuditService auditService) {
        this.wikiService = wikiService;
        this.mapper = mapper;
        this.revisionService = revisionService;
        this.auditService = auditService;
    }

    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : "SYSTEM";
    }

    @PostMapping
    public ResponseEntity<WikiPageResponse> createPage(@Valid @RequestBody CreateWikiPageRequest request) {
        WikiPage created = wikiService.createPage(mapper.toDomain(request));
        auditService.log("CREATE", created.getId(), currentUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping
    public Page<WikiPageResponse> list(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return wikiService.listPages(pageable).map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    public WikiPageResponse getPageById(@PathVariable String id) {
        return mapper.toResponse(wikiService.getPageById(id));
    }

    @GetMapping("/search")
    public Page<WikiPageResponse> searchByTitle(@RequestParam("title") String fragment,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return wikiService.searchByTitle(fragment, pageable).map(mapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        wikiService.deletePage(id);
        auditService.log("DELETE", id, currentUsername());
    }

    @PutMapping("/{id}")
    public WikiPageResponse update(@PathVariable String id,
                                   @Valid @RequestBody CreateWikiPageRequest request) {

        WikiPage current = wikiService.getPageById(id);
        revisionService.createSnapshot(current, currentUsername());

        WikiPage updated = wikiService.updatePage(id, mapper.toDomain(request));
        auditService.log("UPDATE", id, currentUsername());

        return mapper.toResponse(updated);
    }
}