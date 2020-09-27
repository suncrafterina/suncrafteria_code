package suncrafterina.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FaqsDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String answer;

    public FaqsDTO() {}

    public FaqsDTO(@NotBlank Long id, @NotNull @NotBlank String title, @NotNull @NotBlank String answer) {

        this.id = id;
        this.title = title;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "FaqsDTO [id=" + id + ", title=" + title + ", answer=" + answer + "]";
    }

}
