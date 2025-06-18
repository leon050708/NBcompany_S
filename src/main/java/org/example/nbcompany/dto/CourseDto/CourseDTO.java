package org.example.nbcompany.dto.CourseDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseDTO {
    private Long id;
    private String courseName;
    private String coverImageUrl;
    private String summary;
    private String courseVideoUrl;
    private Integer sortOrder;
    private Long authorId;
    private String authorName;
    private Long companyId;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CourseDTO(Long id, String courseName, String coverImageUrl, String summary, String courseVideoUrl, Integer sortOrder, Long authorId, String authorName, Long companyId, Integer status, Integer viewCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.courseName = courseName;
        this.coverImageUrl = coverImageUrl;
        this.summary = summary;
        this.courseVideoUrl = courseVideoUrl;
        this.sortOrder = sortOrder;
        this.authorId = authorId;
        this.authorName = authorName;
        this.companyId = companyId;
        this.status = status;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CourseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCourseVideoUrl() {
        return courseVideoUrl;
    }

    public void setCourseVideoUrl(String courseVideoUrl) {
        this.courseVideoUrl = courseVideoUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
