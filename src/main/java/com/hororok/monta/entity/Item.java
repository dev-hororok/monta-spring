package com.hororok.monta.entity;

import com.hororok.monta.dto.request.item.PatchItemRequestDto;
import com.hororok.monta.dto.request.item.PostItemRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`item`")
@SQLDelete(sql = "UPDATE item SET deleted_at = CURRENT_TIMESTAMP WHERE item_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Item extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long id;

    @NotNull
    @Column(length = 20)
    private String itemType;

    @NotNull
    @Column(length = 100)
    private String name;

    @Column(length = 20)
    private String grade;

    @NotNull
    private String description;

    @NotNull
    private String imageUrl;

    @NotNull
    private int cost;

    private Integer requiredStudyTime;

    @NotNull
    private int effectCode;

    @NotNull
    private Boolean isHidden;

    @OneToMany(mappedBy = "item")
    private List<ItemInventory> itemInventories = new ArrayList<>();

    public Item(PostItemRequestDto requestDto) {
        itemType = requestDto.getItemType();
        name = requestDto.getName();
        grade = requestDto.getGrade();
        description = requestDto.getDescription();
        imageUrl = requestDto.getImageUrl();
        cost = requestDto.getCost();
        requiredStudyTime = requestDto.getRequiredStudyTime();
        effectCode = requestDto.getEffectCode();
        isHidden = requestDto.getIsHidden();
    }

    public void updateItem(String itemType, String name, String grade, String description, String imageUrl, Integer cost,
                           Integer requiredStudyTime, Integer effectCode, Boolean isHidden) {
        this.itemType = itemType;
        this.name = name;
        this.grade = grade;
        this.description = description;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.requiredStudyTime = requiredStudyTime;
        this.effectCode = effectCode;
        this.isHidden = isHidden;
    }
}
