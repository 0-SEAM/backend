package com.seam.service;

import com.seam.entity.GuideContent;
import com.seam.repository.GuideContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuideContentServiceTest {

    @Mock
    private GuideContentRepository repo;

    private GuideContentService service;

    @BeforeEach
    void setUp() {
        service = new GuideContentService(repo);
    }

    @Test
    void listByType_shouldReturnAllWhenTypeIsNull() {
        GuideContent one = new GuideContent();
        GuideContent two = new GuideContent();
        when(repo.findAll()).thenReturn(List.of(one, two));

        List<GuideContent> result = service.listByType(null);

        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    void listByType_shouldFilterByContentTypeWhenTypeIsProvided() {
        GuideContent guide = new GuideContent();
        when(repo.findByContentType("FAQ")).thenReturn(List.of(guide));

        List<GuideContent> result = service.listByType("FAQ");

        assertEquals(1, result.size());
        assertEquals(guide, result.get(0));
        verify(repo).findByContentType("FAQ");
    }
}
