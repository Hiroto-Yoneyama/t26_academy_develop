package jp.co.metateam.library.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.repository.BookMstRepository;

@Service
@SuppressWarnings("unused") // 使っていないインポート（OptionalやRedirectAttributesなど）の黄色い線を消します
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
     * 書籍登録（バリデーションなし・DB保存のみ）
     */
    @Transactional
    public void save(BookMstDto bookMstDto) {
        // 保存用のエンティティを作成
        BookMst bookMst = new BookMst();

        // 画面（Dto）から送られてきた値をセット
        bookMst.setTitle(bookMstDto.getTitle());
        bookMst.setIsbn(bookMstDto.getIsbn());

        // リポジトリ経由でDBに保存
        this.bookMstRepository.save(bookMst);
    }
}