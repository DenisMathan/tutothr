package tutothr.chapter;

import java.util.List;

/**
 * View-Model für die Anzeige eines Kapitels.
 * Kombiniert die Kapitel-Daten mit Zugriffsinformationen für den aktuellen User.
 */
public class ChapterViewModel {
    
    private final ChapterDTO chapter;
    private final boolean accessible;
    private final boolean purchasable;
    
    public ChapterViewModel(ChapterDTO chapter, boolean accessible, boolean purchasable) {
        this.chapter = chapter;
        this.accessible = accessible;
        this.purchasable = purchasable;
    }
    
    // Delegate-Methoden für einfachen Zugriff in Thymeleaf
    public Long getId() {
        return chapter.getId();
    }
    
    public String getTitle() {
        return chapter.getTitle();
    }
    
    public String getDescription() {
        return chapter.getDescription();
    }
    
    public int getPosition() {
        return chapter.getPosition();
    }
    
    public boolean isPaywalled() {
        return chapter.isPaywalled();
    }
    
    public Float getPrice() {
        return chapter.getPrice();
    }
    
    public List<String> getAttachmentUrls() {
        return chapter.getAttachmentUrls();
    }
    
    // ViewModel-spezifische Felder
    public boolean isAccessible() {
        return accessible;
    }
    
    public boolean isPurchasable() {
        return purchasable;
    }
    
    // Für Formulare etc.
    public ChapterDTO getChapter() {
        return chapter;
    }
    
    public java.util.Map<String, String> getValidationErrors() {
        return chapter.getValidationErrors();
    }
}