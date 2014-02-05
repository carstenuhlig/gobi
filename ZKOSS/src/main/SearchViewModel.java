package main;

import data.Gen;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import java.util.List;

public class SearchViewModel {

    private String keyword;
    private List<Gen> genList;
    private Gen selectedGen;
    private String statusMessage;

    private GenService genService = new GenServiceImpl();

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

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
        status();
    }

    @NotifyChange("statusMessage")
    public void status() {
        statusMessage = genService.getStatus();
    }
}
