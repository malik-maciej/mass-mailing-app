package pl.wsb.massmailingapp.dto;

public class MailFormDTO {

    private String subject;
    private String content;
    private int count;

    public MailFormDTO(String subject, String content, int count) {
        this.subject = subject;
        this.content = content;
        this.count = count;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public int getCount() {
        return count;
    }
}