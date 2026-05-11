package jp.co.metateam.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.service.BookMstService;
import lombok.extern.log4j.Log4j2;

/**
 * 書籍関連クラス
 */
@Log4j2 
@Controller
@SuppressWarnings("unused")
public class BookController {

    private final BookMstService bookMstService;

    @Autowired
    public BookController(BookMstService bookMstService){
        this.bookMstService = bookMstService;
    }

    /**
     * 書籍一覧画面
     */
    @GetMapping("/book/index")
    public String index(Model model) {
        List<BookMstDto> bookMstList = this.bookMstService.findAvailableWithStockCount();
        model.addAttribute("bookMstList", bookMstList);
        return "book/index";
    }

    /**
     * 書籍登録画面表示
     */
    @GetMapping("/book/add")
    public String add(Model model) {
        if (!model.containsAttribute("bookMstDto")) {
            model.addAttribute("bookMstDto", new BookMstDto());
        }
        return "book/add";
    }

    /**
     * 書籍登録処理
     */
    @PostMapping("/book/add")
    public String addBook(
        @ModelAttribute @Valid BookMstDto bookMstDto, 
        BindingResult result, 
        RedirectAttributes redirectAttributes, 
        Model model
    ) {
        // 1. DTO側で設定したバリデーション（必須、文字数、数字形式）に引っかかった場合
        if (result.hasErrors()) {
            // ★追加：HTMLの ${errTitle} や ${errISBN} にメッセージを詰め替える
            if (result.hasFieldErrors("title")) {
                model.addAttribute("errTitle", result.getFieldError("title").getDefaultMessage());
            }
            if (result.hasFieldErrors("isbn")) {
                model.addAttribute("errISBN", result.getFieldError("isbn").getDefaultMessage());
            }
            return "book/add";
        }

        try {
            // 2. 保存処理
            this.bookMstService.save(bookMstDto);
            redirectAttributes.addFlashAttribute("message", "書籍を登録しました");
            return "redirect:index";

        } catch (Exception e) {
            // 3. 重複チェック（DB照合）などの例外エラー
            model.addAttribute("errISBN", "登録済みのISBNです");
            return "book/add";
        }
    }
}