package fr.mycellius.web.mapper;

import fr.mycellius.domain.Tag;
import fr.mycellius.domain.WikiPage;
import fr.mycellius.web.dto.CreateWikiPageRequest;
import fr.mycellius.web.dto.WikiPageResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-11T22:29:38+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class WikiPageDtoMapperImpl implements WikiPageDtoMapper {

    @Override
    public WikiPage toDomain(CreateWikiPageRequest request) {
        if ( request == null ) {
            return null;
        }

        WikiPage wikiPage = new WikiPage();

        wikiPage.setId( request.id() );
        wikiPage.setTitle( request.title() );
        wikiPage.setContent( request.content() );
        wikiPage.setTags( toDomainTags( request.tags() ) );

        return wikiPage;
    }

    @Override
    public WikiPageResponse toResponse(WikiPage page) {
        if ( page == null ) {
            return null;
        }

        String id = null;
        String title = null;
        String content = null;
        List<String> tags = null;
        Instant createdAt = null;

        id = page.getId();
        title = page.getTitle();
        content = page.getContent();
        tags = tagListToStringList( page.getTags() );
        createdAt = page.getCreatedAt();

        WikiPageResponse wikiPageResponse = new WikiPageResponse( id, title, content, tags, createdAt );

        return wikiPageResponse;
    }

    protected List<String> tagListToStringList(List<Tag> list) {
        if ( list == null ) {
            return null;
        }

        List<String> list1 = new ArrayList<String>( list.size() );
        for ( Tag tag : list ) {
            list1.add( map( tag ) );
        }

        return list1;
    }
}
