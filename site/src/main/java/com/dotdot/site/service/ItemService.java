package com.dotdot.site.service;

import com.dotdot.site.model.Item;
import com.dotdot.site.model.Member;
import com.dotdot.site.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public int nextId() {
        return itemRepository.nextId();
    }

    public void saveItem(Item item, Member member) {
        item.setMember(member);
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }
}
