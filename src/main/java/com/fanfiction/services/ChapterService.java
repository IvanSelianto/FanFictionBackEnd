package com.fanfiction.services;

import com.fanfiction.models.Chapter;
import com.fanfiction.models.Composition;
import com.fanfiction.DTO.ChapterDTO;
import com.fanfiction.repository.ChapterRepository;
import com.fanfiction.repository.CompositionRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChapterService {
    @Autowired
    private CompositionRepository compositionRepository;
    @Autowired
    private ChapterRepository chapterRepository;


    public void saveChapter(ChapterDTO chapterDTO) throws IOException {
        Chapter chapter = new Chapter();
        chapter.setName(chapterDTO.getName());
        chapter.setText(chapterDTO.getText());
        Composition composition = compositionRepository.findById(chapterDTO.getCompositionId()).get();
        composition.setPublicationDate(String.valueOf(new java.sql.Timestamp(new Date().getTime())).replaceAll("\\.\\d+", ""));
        compositionRepository.save(composition);

        chapter.setComposition(composition);
        chapter.setChapterNumber(chapterDTO.getChapterNumber());
        chapter.setId(chapterDTO.getId());

        if (chapterDTO.getImgUrl() != null) {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "seliantocloudinary",
                    "api_key", "462586271919223",
                    "api_secret", "iNj2b3c_XbG7D7FP9caNrWTdT8A"));
            Map params = ObjectUtils.asMap(
                    "public_id", chapter.toString(),
                    "overwrite", true,
                    "notification_url", "https://api.cloudinary.com/v1_1/seliantocloudinary/image/upload",
                    "resource_type", "image"
            );
            chapter.setImgUrl(cloudinary.uploader().upload(chapterDTO.getImgUrl(), params).get("secure_url").toString());
        }
        chapterRepository.save(chapter);
    }

    public List<Chapter> findAllChaptersByCompositionId(Long compositionId) {
        return chapterRepository.findAllByCompositionId(compositionId).stream()
                .sorted((chapter1, chapter2) -> Integer.parseInt(String.valueOf(chapter1.getChapterNumber() - chapter2.getChapterNumber()))).collect(Collectors.toList());
    }

    public void deleteChapter(Chapter chapter) {
        chapterRepository.delete(chapter);
        saveChapters(findAllChaptersByCompositionId(chapter.getComposition().getId()));
    }


    public void saveChapters(List<Chapter> chapters) {
        for (int i = 0; i < chapters.size(); i++) {
            chapters.get(i).setChapterNumber(i + 1);
        }
        chapterRepository.saveAll(chapters);
    }

}
