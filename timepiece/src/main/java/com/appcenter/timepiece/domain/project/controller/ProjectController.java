package com.appcenter.timepiece.domain.project.controller;

import com.appcenter.timepiece.domain.project.dto.ProjectCreateUpdateRequest;
import com.appcenter.timepiece.domain.project.dto.TransferPrivilegeRequest;
import com.appcenter.timepiece.domain.project.service.ProjectService;
import com.appcenter.timepiece.global.common.dto.CommonResponse;
import com.appcenter.timepiece.global.config.SwaggerApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("")
@RestController
@SwaggerApiResponses
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/v1/projects/{projectId}")
    @Operation(summary = "프로젝트 정보 조회", description = "프로젝트의 상세 정보를 조회합니다.")
    public CommonResponse<?> findProject(@PathVariable Long projectId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("프로젝트 조회 성공", projectService.findProject(projectId, userDetails));
    }

    @GetMapping("/v1/projects/members/{memberId}")
    @Operation(summary = "소속 프로젝트 전체 조회(썸네일)", description = "")
    public CommonResponse<?> findProjects(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "6", required = false) Integer size,
            @PathVariable Long memberId, @AuthenticationPrincipal UserDetails userDetails) {

        return CommonResponse.success("프로젝트 목록 조회 성공",
                projectService.findProjects(page, size, memberId, userDetails));
    }

    @GetMapping("/v1/projects/members/{memberId}/pin")
    @Operation(summary = "핀 설정된 프로젝트 조회(+시간표)", description = "")
    public CommonResponse<?> findPinProjects(@PathVariable Long memberId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("핀 설정된 프로젝트 조회 성공",
                projectService.findPinProjects(memberId, userDetails));
    }

    @GetMapping("/v1/projects/members/{memberId}/{keyword}")
    @Operation(summary = "프로젝트 검색", description = "", deprecated = true)
    public CommonResponse<?> searchProjects(@RequestParam(defaultValue = "0", required = false) Integer page,
                                            @RequestParam(defaultValue = "6", required = false) Integer size,
                                            @PathVariable Long memberId,
                                            @PathVariable String keyword,
                                            @RequestParam(defaultValue = "false", required = false) Boolean isStored,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("프로젝트 검색 성공",
                projectService.searchProjects(page, size, isStored, memberId, keyword, userDetails));
    }

    @GetMapping("/v2/projects/members/{memberId}")
    @Operation(summary = "프로젝트 검색", description = "")
    public CommonResponse<?> searchProjectsParam(@RequestParam(defaultValue = "0", required = false) Integer page,
                                                 @RequestParam(defaultValue = "6", required = false) Integer size,
                                                 @RequestParam String keyword,
                                                 @PathVariable Long memberId,
                                                 @RequestParam(defaultValue = "false", required = false) Boolean isStored,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("프로젝트 검색 성공",
                projectService.searchProjects(page, size, isStored, memberId, keyword, userDetails));
    }

    // TODO: 해당 기능은 PROJECT가 아닌 MEMBER의 책임이 아닐까?
    @GetMapping("/v1/projects/{projectId}/members")
    @Operation(summary = "프로젝트에 속해있는 유저 전체 조회", description = "")
    public CommonResponse<?> findMembersInProject(@PathVariable Long projectId,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("프로젝트 내 사용자 조회 성공",
                projectService.findMembers(projectId, userDetails));
    }

    @GetMapping("/v1/projects/stored")
    @Operation(summary = "보관 프로젝트 목록 조회", description = "")
    public CommonResponse<?> findStoredProjects(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "9", required = false) Integer size,
            @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("보관 프로젝트 목록 조회 성공",
                projectService.findStoredProjects(page, size, userDetails));
    }

    @PostMapping("/v1/projects")
    @Operation(summary = "프로젝트 생성", description = "")
    public ResponseEntity<CommonResponse<?>> createProject(@RequestBody @Valid ProjectCreateUpdateRequest request,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        projectService.createProject(request, userDetails);
        return ResponseEntity.ok().body(CommonResponse.success("프로젝트 생성 성공", null));
    }

    @DeleteMapping("/v1/projects/{projectId}")
    @Operation(summary = "프로젝트 삭제", description = "")
    public CommonResponse<?> kickProject(@PathVariable Long projectId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        projectService.deleteProject(projectId, userDetails);
        return CommonResponse.success("프로젝트 삭제 성공", null);
    }

    @PutMapping("/v1/projects/{projectId}")
    @Operation(summary = "프로젝트 수정", description = "")
    public CommonResponse<?> updateProject(@PathVariable Long projectId,
                                           @RequestBody @Valid ProjectCreateUpdateRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        projectService.updateProject(projectId, request, userDetails);
        return CommonResponse.success("프로젝트 수정 성공", null);
    }

    @PostMapping("/v1/projects/{projectId}/invitation")
    @Operation(summary = "회원 초대 링크 생성", description = "")
    public CommonResponse<?> generateInviteLink(@PathVariable Long projectId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        return CommonResponse.success("초대링크를 성공적으로 생성했습니다",
                projectService.generateInviteLink(projectId, userDetails));
    }

    @GetMapping("/v1/projects/invitations")
    @Operation(summary = "초대 링크 메타데이터 조회", description = "")
    public CommonResponse<?> generateInviteLink(@RequestParam(required = true) String url) {
        return CommonResponse.success("초대링크 정보를 성공적으로 조회했습니다",
                projectService.decodeInviteLink(url));
    }

    @DeleteMapping("/v1/projects/{projectId}/members/{memberId}")
    @Operation(summary = "회원 강퇴", description = "")
    public CommonResponse<Void> kick(@PathVariable Long projectId,
                                     @PathVariable Long memberId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        projectService.kick(projectId, memberId, userDetails);
        return CommonResponse.success("추방되었습니다", null);
    }

    @DeleteMapping("/v1/projects/{projectId}/me")
    @Operation(summary = "프로젝트 나가기", description = "")
    public CommonResponse<Void> goOut(@PathVariable Long projectId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        projectService.goOut(projectId, userDetails);
        return CommonResponse.success("프로젝트에서 나갔습니다", null);
    }

    @PatchMapping("/v1/projects/{projectId}/transfer-privilege")
    @Operation(summary = "관리자 권한 양도", description = "")
    public CommonResponse<Void> transferPrivilege(@PathVariable Long projectId,
                                                  @RequestBody @Valid TransferPrivilegeRequest request,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        projectService.transferPrivilege(projectId, request, userDetails);
        return CommonResponse.success("프로젝트 관리 권한을 양도하였습니다.", null);
    }

    @PostMapping("/v1/invitation/{url}")
    @Operation(summary = "회원 초대(추가)", description = "")
    public CommonResponse<Void> join(@PathVariable String url,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        projectService.addUserToGroup(url, userDetails);
        return CommonResponse.success("프로젝트 멤버로 추가 되었습니다", null);
    }

    @GetMapping("/v1/covers")
    @Operation(summary = "커버 이미지 메타데이터 조회", description = "")
    public CommonResponse<?> getCoverMetadata(@RequestParam(defaultValue = "0", required = false) Integer page,
                                              @RequestParam(defaultValue = "10", required = false) Integer size) {
        return CommonResponse.success("조회 요청이 성공했습니다.", projectService.getCoverMetadata(page, size));
    }
}

