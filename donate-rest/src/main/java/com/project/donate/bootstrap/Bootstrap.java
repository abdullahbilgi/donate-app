package com.project.donate.bootstrap;


import com.project.donate.model.City;
import com.project.donate.model.Region;
import com.project.donate.repository.CityRepository;
import com.project.donate.repository.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Profile("bootstrap")
@Transactional
public class Bootstrap implements CommandLineRunner {


    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;

    public Bootstrap(CityRepository cityRepository, RegionRepository regionRepository) {
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        if (cityRepository.count() < 1) {

            List<City> cities = List.of(
                    new City("ADANA"),
                    new City("ADIYAMAN"),
                    new City("AFYONKARAHİSAR"),
                    new City("AĞRI"),
                    new City("AMASYA"),
                    new City("ANKARA"),
                    new City("ANTALYA"),
                    new City("ARTVİN"),
                    new City("AYDIN"),
                    new City("BALIKESİR"),
                    new City("BİLECİK"),
                    new City("BİNGÖL"),
                    new City("BİTLİS"),
                    new City("BOLU"),
                    new City("BURDUR"),
                    new City("BURSA"),
                    new City("ÇANAKKALE"),
                    new City("ÇANKIRI"),
                    new City("ÇORUM"),
                    new City("DENİZLİ"),
                    new City("DİYARBAKIR"),
                    new City("EDİRNE"),
                    new City("ELAZIĞ"),
                    new City("ERZİNCAN"),
                    new City("ERZURUM"),
                    new City("ESKİŞEHİR"),
                    new City("GAZİANTEP"),
                    new City("GİRESUN"),
                    new City("GÜMÜŞHANE"),
                    new City("HAKKARİ"),
                    new City("HATAY"),
                    new City("ISPARTA"),
                    new City("MERSİN"),
                    new City("İSTANBUL"),
                    new City("İZMİR"),
                    new City("KARS"),
                    new City("KASTAMONU"),
                    new City("KAYSERİ"),
                    new City("KIRKLARELİ"),
                    new City("KIRŞEHİR"),
                    new City("KOCAELİ"),
                    new City("KONYA"),
                    new City("KÜTAHYA"),
                    new City("MALATYA"),
                    new City("MANİSA"),
                    new City("KAHRAMANMARAŞ"),
                    new City("MARDİN"),
                    new City("MUĞLA"),
                    new City("MUŞ"),
                    new City("NEVŞEHİR"),
                    new City("NİĞDE"),
                    new City("ORDU"),
                    new City("RİZE"),
                    new City("SAKARYA"),
                    new City("SAMSUN"),
                    new City("SİİRT"),
                    new City("SİNOP"),
                    new City("SİVAS"),
                    new City("TEKİRDAĞ"),
                    new City("TOKAT"),
                    new City("TRABZON"),
                    new City("TUNCELİ"),
                    new City("ŞANLIURFA"),
                    new City("UŞAK"),
                    new City("VAN"),
                    new City("YOZGAT"),
                    new City("ZONGULDAK"),
                    new City("AKSARAY"),
                    new City("BAYBURT"),
                    new City("KARAMAN"),
                    new City("KIRIKKALE"),
                    new City("BATMAN"),
                    new City("ŞIRNAK"),
                    new City("BARTIN"),
                    new City("ARDAHAN"),
                    new City("IĞDIR"),
                    new City("YALOVA"),
                    new City("KARABÜK"),
                    new City("KİLİS"),
                    new City("OSMANİYE"),
                    new City("DÜZCE")
            );

            cityRepository.saveAll(cities);

        }




    }
}
