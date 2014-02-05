package main;

import data.Gen;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import java.util.List;

public class SearchViewModel {

    private String keyword;
    private List<Gen> genList;
    private Gen selectedGen;

    private GenService genService = new GenServiceImpl();

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public List<Gen> getGenList() {
        return genList;
    }


    public void setSelectedGen(Gen selectedGen) {
        this.selectedGen = selectedGen;
    }

    public Gen getSelectedGen() {
        return selectedGen;
    }


    @Command
    @NotifyChange("genList")
    public void search() {
        genList = genService.search(keyword);
    }
}
