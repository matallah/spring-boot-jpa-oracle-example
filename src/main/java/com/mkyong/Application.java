package com.mkyong;

import com.mkyong.dao.ItemspathRepo;
import com.mkyong.dao.KnowledgepoolRepo;
import com.mkyong.dao.LinksRepository;
import com.mkyong.model.Itemspaths;
import com.mkyong.model.Knowledgepool;
import com.mkyong.model.LinksLabels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.exit;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    DataSource dataSource;

    @Autowired
    LinksRepository linksRepository;
    @Autowired
    ItemspathRepo itemspathRepo;
    @Autowired
    KnowledgepoolRepo knowledgepoolRepo;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    final String CSV_LOCATION = "Data.csv ";

    @Override
    public void run(String... args) throws Exception {
        long currTime = System.currentTimeMillis();
        System.out.println("DATASOURCE = " + dataSource);
        Collection<LinksLabels> values = null;
        short parentType = 2;
        System.out.println("Start Retrieving with ParentType: " + parentType + " ************************");
        values = linksRepository.findAllByParenttype(parentType);
        System.out.println("End Retrieving ************************ " + values.size());
        int index = 0;
        if (parentType == 2) {
            for (LinksLabels value : values) {
                ++index;
                System.out.println("Start index ************************" + index + " *** ItemId: " + value.getItemid() + " *** parentId: " + value.getParentid() + " *** type: " + value.getType());
                Itemspaths itemsPathsPojo = new Itemspaths();
                itemsPathsPojo.setItemid(value.getItemid());
                itemsPathsPojo.setDirectparentid(value.getParentid());
                itemsPathsPojo.setDirectparenttype(BigInteger.valueOf(value.getParenttype()));
                values.stream().filter(linksLabels -> linksLabels.getItemid().equals(value.getParentid())).map(LinksLabels::getLabel).forEachOrdered(itemsPathsPojo::setParentlabel);
                StringBuilder itemFullPathBuilder = new StringBuilder(value.getLabel() + "~");
                StringBuilder itemfullpathidsBuilder = new StringBuilder(value.getItemid() + "~");
                StringBuilder iullparenttypeBuilder = new StringBuilder(value.getType() + "~");
                this.recursionFullParent(itemFullPathBuilder, itemfullpathidsBuilder, iullparenttypeBuilder, value, value.getParentid());
                itemsPathsPojo.setItemfullpath(itemFullPathBuilder.toString());//full path labels
                itemsPathsPojo.setItemfullpathids(itemfullpathidsBuilder.toString());//full path ids
                itemsPathsPojo.setFullparenttype(iullparenttypeBuilder.toString());//full path ids
                itemsPathsPojo.setRootparent(itemfullpathidsBuilder.toString().split("~")[itemfullpathidsBuilder.toString().split("~").length - 1]);//kp id
                itemspathRepo.save(itemsPathsPojo);
                Short type = value.getType();
            }
        } else {
            List<Knowledgepool> knowledgepoolRepoAll = knowledgepoolRepo.findAll();
            values.forEach(value -> {
                Itemspaths itemsPathsPojo = new Itemspaths();
                itemsPathsPojo.setItemid(value.getItemid());
                itemsPathsPojo.setDirectparentid(value.getParentid());
                itemsPathsPojo.setDirectparenttype(BigInteger.valueOf(value.getParenttype()));
                knowledgepoolRepoAll.stream().filter(knowledgepool -> value.getParentid().equals(knowledgepool.getPoolid())).forEach(knowledgepool -> {
                    itemsPathsPojo.setParentlabel(knowledgepool.getName());
                    itemsPathsPojo.setItemfullpath(value.getLabel() + "~" + knowledgepool.getName());//full path labels
                    itemsPathsPojo.setItemfullpathids(value.getItemid() + "~" + knowledgepool.getPoolid());//full path ids
                    itemsPathsPojo.setFullparenttype(value.getType() + "~" + 0);//full path ids
                    itemsPathsPojo.setRootparent(knowledgepool.getPoolid());//kp id
                });
                itemspathRepo.save(itemsPathsPojo);
            });
        }
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - currTime) / 1000.0;
        System.out.println("Time Native ***************** :" + elapsedTime);
        System.out.println("Done!");
        long currentTimeA = System.currentTimeMillis();
        double elapsedTimeA = (currentTimeA - currTime) / 1000.0;
        System.out.println("All Time ***************** :" + elapsedTimeA);
        exit(0);
    }

    private void recursionFullParent(StringBuilder itemFullPathBuilder, StringBuilder itemfullpathidsBuilder, StringBuilder iullparenttypeBuilder, LinksLabels linksLabels, String parentid) {
        List<LinksLabels> allByItemid = linksRepository.findAllByItemid(parentid);
        LinksLabels linksByParentId = !allByItemid.isEmpty() ? allByItemid.get(0) : null;
        if (linksByParentId != null) {
            itemFullPathBuilder.append(linksByParentId.getLabel()).append("~");
            itemfullpathidsBuilder.append(linksByParentId.getItemid()).append("~");
            iullparenttypeBuilder.append(linksByParentId.getType() != null ? linksByParentId.getType() : 1).append("~");
            recursionFullParent(itemFullPathBuilder, itemfullpathidsBuilder, iullparenttypeBuilder, linksLabels, linksByParentId.getParentid());
        }
//        if (linksLabelsHashMap.containsKey(parentid)) {
//            LinksLabels parent = linksLabelsHashMap.get(parentid);
//            rootParent = parent.getParentid();
//            parentid = parent.getParentid();
//            if (parentid != null) {
//                itemFullPathBuilder.append(parent.getLabel()).append("~");
//                itemfullpathidsBuilder.append(parent.getParentid()).append("~");
//                iullparenttypeBuilder.append(parent.getParenttype()).append("~");
//                recursionFullParent(itemFullPathBuilder, itemfullpathidsBuilder, iullparenttypeBuilder, rootParent, linksLabels, parentid);
//            } else {
//                itemFullPathBuilder.append(parent.getLabel());
//                itemfullpathidsBuilder.append(linksLabels.getParentid());
//                iullparenttypeBuilder.append("0");
//                rootParent = linksLabels.getParentid();
//            }
//        }
    }


//    @Transactional(readOnly = true)
//    @Override
//    public void run(String... args) throws Exception {
//
//        System.out.println("DATASOURCE = " + dataSource);
////        List<LinksLabels> top100By = linksRepository.findTop100By();
//        List<LinksLabels> linksLabels = (List<LinksLabels>) linksRepository.findAll();
//        FileWriter writer = new
//                FileWriter(CSV_LOCATION);
//        // Create Mapping Strategy to arrange the
//        // column name in order
//        ColumnPositionMappingStrategy mappingStrategy =
//                new ColumnPositionMappingStrategy();
//        mappingStrategy.setType(LinksLabels.class);
//
//        // Arrange column name as provided in below array.
//        String[] columns = new String[]
//                {"itemid", "parentid", "pos", "parenttype", "indexclass", "label"};
//        mappingStrategy.setColumnMapping(columns);
//
//        // Createing StatefulBeanToCsv object
//        StatefulBeanToCsvBuilder<LinksLabels> builder =
//                new StatefulBeanToCsvBuilder(writer);
//        StatefulBeanToCsv beanWriter =
//                builder.withMappingStrategy(mappingStrategy).build();
//
//        // Write list to StatefulBeanToCsv object
//        beanWriter.write(linksLabels);
//        // closing the writer object
//        writer.close();
//        System.out.println("Done!");
//
//        exit(0);
//    }

}