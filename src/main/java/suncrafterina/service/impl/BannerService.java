package suncrafterina.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import suncrafterina.domain.Banner;
import suncrafterina.repository.BannerRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BannerService {

    @Autowired
    BannerRepository bannerRepository;

    public Banner saveBanner(Banner banner){
         bannerRepository.save(banner);
         return banner;
    }

    public Banner findBannerById(Long id){
        Optional<Banner> banner = bannerRepository.findBannerById(id);
        if(banner.isPresent())
            return banner.get();
        return null;
    }

    public Page<Object[]> getAllBanner(Pageable pageable){
        return bannerRepository.findALlBanner(pageable);
    }

    public Boolean deleteBanner(Banner banner){
        bannerRepository.delete(banner);
        return true;
    }

    public Page<Object[]> getAllBannerList(String search,Pageable pageable){
        return bannerRepository.findAllBannerList(search,pageable);
    }
}
