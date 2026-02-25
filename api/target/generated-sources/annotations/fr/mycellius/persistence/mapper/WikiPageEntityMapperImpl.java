package fr.mycellius.persistence.mapper;

import fr.mycellius.domain.WikiPage;
import fr.mycellius.persistence.entity.WikiPageEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-25T22:54:31+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class WikiPageEntityMapperImpl implements WikiPageEntityMapper {

    @Override
    public WikiPage toDomain(WikiPageEntity entity) {
        if ( entity == null ) {
            return null;
        }

        WikiPage wikiPage = new WikiPage();

        wikiPage.setId( entity.getId() );
        wikiPage.setTitle( entity.getTitle() );
        wikiPage.setContent( entity.getContent() );
        wikiPage.setCreatedAt( entity.getCreatedAt() );

        wikiPage.setTags( toDomainTags(entity.getTags()) );

        return wikiPage;
    }

    @Override
    public WikiPageEntity toEntity(WikiPage page) {
        if ( page == null ) {
            return null;
        }

        WikiPageEntity wikiPageEntity = new WikiPageEntity();

        wikiPageEntity.setId( page.getId() );
        wikiPageEntity.setTitle( page.getTitle() );
        wikiPageEntity.setContent( page.getContent() );
        wikiPageEntity.setCreatedAt( page.getCreatedAt() );

        return wikiPageEntity;
    }
}
