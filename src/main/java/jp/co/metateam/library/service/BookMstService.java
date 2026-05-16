package jp.co.metateam.library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // ★追加が必要

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.repository.BookMstRepository;

@Service
@SuppressWarnings("unused")
public class BookMstService {

    private final BookMstRepository bookMstRepository;
    
    @Autowired
    public BookMstService(BookMstRepository bookMstRepository){
        this.bookMstRepository = bookMstRepository;
    }
    
    /**
     * 利用可能な書籍一覧を取得
     */
    public List<BookMstDto> findAvailableWithStockCount() {
        List<BookMst> books = this.bookMstRepository.findLimitedBook();
        List<BookMstDto> bookMstDtoList = new ArrayList<BookMstDto>();

        for (int i = 0; i < books.size(); i++) {
            BookMst book = books.get(i);
            BookMstDto bookMstDto = new BookMstDto();

            bookMstDto.setId(book.getId());
            bookMstDto.setIsbn(book.getIsbn());
            bookMstDto.setTitle(book.getTitle());

            bookMstDtoList.add(bookMstDto);
        }

        return bookMstDtoList;
    }

    /**
     * ★ここを修正：ISBN（String）で重複を確認する
     */
    public boolean existsByIsbn(String isbn) {
        // existsById は数字(Long)用なので、findByIsbn を使う
        return this.bookMstRepository.findByIsbn(isbn).isPresent();
    }

    /**
     * 書籍登録
     */
    @Transactional
    public void save(BookMstDto bookMstDto) {
        BookMst bookMst = new BookMst();
        bookMst.setTitle(bookMstDto.getTitle());
        bookMst.setIsbn(bookMstDto.getIsbn());
        this.bookMstRepository.save(bookMst);
    }
}