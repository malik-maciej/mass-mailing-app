package pl.wsb.massmailingapp.dto;

public class MailFormDTO {

    private String subject;
    private String content;
    private int count;

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