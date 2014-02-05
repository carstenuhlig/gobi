package data;

public class Gen {

    private Integer id;
    private String geneid;
    private Integer transcripts;

    public Gen() {
    }

    public Gen(Integer id, String geneid, Integer transcripts) {
        this.id = id;
        this.geneid = geneid;
        this.transcripts = transcripts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTranscripts() {
        return transcripts;
    }

    public void setTranscripts(Integer transcripts) {
        this.transcripts = transcripts;
    }

    public String getGeneid() {
        return geneid;
    }

    public void setGeneid(String geneid) {
        this.geneid = geneid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Gen))
            return false;
        Gen other = (Gen) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
