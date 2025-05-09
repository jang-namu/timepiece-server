package com.appcenter.timepiece.domain.project.service;

import com.appcenter.timepiece.domain.project.entity.Cover;
import com.appcenter.timepiece.domain.project.repository.CoverRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final RedisTemplate<String, byte[]> redisImageTemplate;
    private final CoverRepository coverRepository;

    @Value("${server.host}")
    private String host;

    public void uploadImage(MultipartFile thumbnail, MultipartFile coverImage) throws IOException {
        String thumbnailName = UUID.randomUUID() + "_" + thumbnail.getOriginalFilename();
        String coverName = UUID.randomUUID() + "_" + coverImage.getOriginalFilename();
        Cover cover = Cover.of(host + "/cover-image/" + thumbnailName, host + "/cover-image/" + coverName);
        byte[] thumbnailImageData = thumbnail.getBytes();
        byte[] coverImageData = coverImage.getBytes();
        redisImageTemplate.opsForValue().set(thumbnailName, thumbnailImageData);
        redisImageTemplate.opsForValue().set(coverName, coverImageData);
        coverRepository.save(cover);
    }

    public Optional<byte[]> getImage(String id) {
        return Optional.ofNullable(redisImageTemplate.opsForValue().get(id));
    }

    public MediaType getImageType(String id) {
        // 파일 확장자에 따라 이미지 타입을 설정합니다. 실제 구현 시 파일 메타데이터를 함께 저장하거나,
        // 파일명에서 확장자를 추출하는 방식으로 구현할 수 있습니다.
        if (id.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (id.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.IMAGE_JPEG; // 기본값
        }
    }
}
