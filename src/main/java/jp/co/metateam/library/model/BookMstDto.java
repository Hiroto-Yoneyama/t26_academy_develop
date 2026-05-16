package jp.co.metateam.library.model;

import java.io.Serializable;
import java.security.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 書籍マスタDTO
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class BookMstDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id; 

    // --- ISBNのルール ---
    @NotBlank(message = "ISBNは必須です")
    @Size(max = 13, message = "ISBNは13文字以下で入力してください")
    @Pattern(regexp = "^[0-9]*$", message = "ISBNは半角数字で設定してください")
    private String isbn;

    // --- 書籍名のルール ---
    @NotBlank(message = "書籍名は必須です")
    @Size(max = 256, message = "書籍名は256文字以下で入力してください")
    private String title;
    
    private Timestamp deletedAt;

    private BookMst bookMst;
}
