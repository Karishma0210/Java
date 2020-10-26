
package karaoke_cw;

public class Song {
    private String title;
    private String artist;
    private int duration; //in seconds
    private String videoFile;

    public Song() {
        title="";
        artist="";
        duration=0;
        videoFile="";
    }

    public Song(String title, String artist, int duration, String videoFile) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.videoFile = videoFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    @Override
    public String toString() {
        
        return title + "\tby\t" + artist ;
    }

}
