package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A") // item 테이블에서 dtype이 "A"로 지정
@Getter @Setter
public class Album extends Item {

    private String artist;
    private String etc;

}
