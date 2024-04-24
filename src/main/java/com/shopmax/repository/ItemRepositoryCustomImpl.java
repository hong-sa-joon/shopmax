package com.shopmax.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shopmax.constant.ItemSellStatus;
import com.shopmax.dto.ItemSearchDto;
import com.shopmax.entity.Item;
import com.shopmax.entity.QItem;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 현재 날짜로부터 이전날짜를 구해주는 메소드
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now(); // 현재 날짜, 시간

        if (StringUtils.equals("all", searchDateType) || searchDateType == null)
            return null;
        else if (StringUtils.equals("1d", searchDateType))
            dateTime = dateTime.minusDays(1); // 1일 전
        else if (StringUtils.equals("1w", searchDateType))
            dateTime = dateTime.minusWeeks(1); // 1주일 전
        else if (StringUtils.equals("1m", searchDateType))
            dateTime = dateTime.minusMonths(1); // 1개월 전
        else if (StringUtils.equals("6m", searchDateType))
            dateTime = dateTime.minusMonths(6); // 6개월 전

        return QItem.item.regTime.after(dateTime);
    }

    // 상태를 전체 했을 때 null이 들어있으므로 처리를 한 번 해준다.
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("itemNm", searchBy)) { // 상품명으로 검색시
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createBy", searchBy)) {
            return QItem.item.createBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        /*
            select * from item
            where reg_time = ?
            and item_sell_status = ?
            and item_nm(create_by) like %검색어%
            order by item_id desc
        */

        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        // pageable 객체: 한 페이지의 몇 개의 게시물을 보여줄지, 시작 페이지 번호에 대한 정보를 가지고 있다.
        return new PageImpl<>(content, pageable, total);
    }
}