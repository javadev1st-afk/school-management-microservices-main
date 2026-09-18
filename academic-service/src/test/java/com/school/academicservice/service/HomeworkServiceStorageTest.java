package com.school.academicservice.service;

import com.school.academicservice.converter.HomeworkConverter;
import com.school.academicservice.dto.HomeworkDTO;
import com.school.academicservice.repository.HomeworkFileRepository;
import com.school.academicservice.repository.HomeworkRepository;
import com.school.common.multitenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeworkServiceStorageTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void createHomeworkStoresFilesOnDiskAndReturnsDownloadUrl() throws Exception {
        HomeworkRepository homeworkRepository = mock(HomeworkRepository.class);
        HomeworkFileRepository homeworkFileRepository = mock(HomeworkFileRepository.class);
        HomeworkService service = new HomeworkService(homeworkRepository, new HomeworkConverter(), homeworkFileRepository);

        Path tempDir = Files.createTempDirectory("hw-storage");
        ReflectionTestUtils.setField(service, "storageBasePath", tempDir.toString());
        ReflectionTestUtils.setField(service, "appBaseUrl", "http://localhost:8002");
        TenantContext.setTenant("SCHOOL_001");

        when(homeworkRepository.save(any())).thenAnswer(invocation -> {
            Object homework = invocation.getArgument(0);
            ReflectionTestUtils.setField(homework, "id", 1L);
            var files = ((com.school.academicservice.entity.Homework) homework).getFiles();
            for (int i = 0; i < files.size(); i++) {
                ReflectionTestUtils.setField(files.get(i), "id", 101L + i);
                files.get(i).setDownloadUrl("http://localhost:8002/api/v1/homework/files/" + (101L + i) + "/download");
            }
            return homework;
        });

        HomeworkDTO request = HomeworkDTO.builder()
                .teacherId(10L)
                .classId(2L)
                .sectionName("A")
                .title("Math assignment")
                .dueDate(LocalDate.now().plusDays(2))
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "files",
                "assignment.pdf",
                "application/pdf",
                "hello-world".getBytes(StandardCharsets.UTF_8)
        );

        HomeworkDTO response = service.createHomework(request, List.of(file));

        assertThat(response.getFiles()).hasSize(1);
        assertThat(response.getFiles().get(0).getDownloadUrl()).contains("/api/v1/homework/files/");
        assertThat(response.getFiles().get(0).getFilePath()).isNotBlank();
        assertThat(Files.exists(Path.of(response.getFiles().get(0).getFilePath()))).isTrue();
    }
}
