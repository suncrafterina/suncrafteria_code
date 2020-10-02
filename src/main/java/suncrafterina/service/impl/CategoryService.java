package suncrafterina.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import suncrafterina.domain.Category;
import suncrafterina.enums.CategoryLevelEnum;
import suncrafterina.repository.CategoryRepository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category){
        categoryRepository.save(category);
        return  category;
    }

    public List<Category> getCateoriesById(Long id){
       return categoryRepository.findCategoriesById(id);
    }

    public Page<Category> getCateoriesByLevel(CategoryLevelEnum id, String search, Pageable pageable){
        return categoryRepository.findCategoriesByLevel(id,search,pageable);
    }

    public Page<Object[]> getCategoriesLevelOne(String search, Pageable pageable){
        return categoryRepository.findCategoriesLevelOne(search,pageable);
    }

    public Category getById(Long id){
        Optional<Category> category= categoryRepository.findById(id);
        if(category.isPresent()){
            return category.get();
        }
        return null;
    }

    public Category getByTitle(String title){
        Optional<Category> category=categoryRepository.findByTitle(title);
        if(category.isPresent()){
            return category.get();
        }
        return null;
    }
    public Category getByIdAndTitle(Long id,String title){
        Optional<Category> category=categoryRepository.findByIdAndTitle(id,title);
        if(category.isPresent()){
            return category.get();
        }
        return null;
    }


    public Page<Object[]> getCategoriesLevelTwo(Long category_id,String search,Pageable pageable){
        return categoryRepository.findCategoriesLevelTwo(category_id,search,pageable);
    }

    /*
    public List<SearchDTO> searchByCategoryName(String search){
        List<Object[]> objects = categoryRepository.searchByCategoryName(search);
        List<SearchDTO> searchDTOS = new LinkedList<>();
        for (Object[] obj : objects){
            BigInteger id = (BigInteger) obj[0];
            String name = (String) obj[1];
            searchDTOS.add(new SearchDTO(id.longValue(),name));
        }
        return searchDTOS;
    }

     */

    public List<Object[]> getCategoriesLevelThree(){
        return  categoryRepository.findCategoriesLevelThree();
    }
}
