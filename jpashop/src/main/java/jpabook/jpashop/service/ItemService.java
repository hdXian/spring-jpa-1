package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.web.BookForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // repo에 로직을 단순 위임함.
    // 상품 저장
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, BookForm updateParam) {
        // findOne을 통해 영속 엔티티를 가져옴
        Book book = (Book) findOne(itemId);
        // 찾는 결과 없으면 예외 터지도록 추가하면 더 좋겠지

        // 영속 엔티티를 수정하여 변경 감지를 이용해 업데이트
        book.setName(updateParam.getName());
        book.setPrice(updateParam.getPrice());
        book.setStockQuantity(updateParam.getStockQuantity());
        book.setAuthor(updateParam.getAuthor());
        book.setIsbn(updateParam.getIsbn());

    }

    // 상품 단건, 전체 조회
    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

}
