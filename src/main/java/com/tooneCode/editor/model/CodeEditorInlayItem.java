package com.tooneCode.editor.model;

import com.tooneCode.completion.model.CompletionRenderType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.Generated;

public class CodeEditorInlayItem {
    private String requestId;
    private String cacheId;
    private int editorOffset;
    private String content;
    private List<EditorInlayItemChunk> chunks;
    private int totalLineCount;
    private boolean rendered = false;
    private long firstDisplayTimeMs;
    private long displayTimeMs;
    private int rankIndex;
    private String batchId;
    private boolean accepted = false;

    public String refactorByAcceptLine() {
        if (this.chunks != null && !this.chunks.isEmpty()) {
            EditorInlayItemChunk chunk = (EditorInlayItemChunk) this.chunks.get(0);
            if (chunk.getCompletionLines() != null && !chunk.getCompletionLines().isEmpty()) {
                String line = (String) chunk.getCompletionLines().get(0);
                if (CompletionRenderType.Inline == chunk.getType()) {
                    this.chunks.remove(0);
                } else if (CompletionRenderType.Block == chunk.getType()) {
                    chunk.getCompletionLines().remove(0);
                }

                this.totalLineCount = 0;
                StringBuilder sb = new StringBuilder();
                Iterator var4 = this.chunks.iterator();

                while (var4.hasNext()) {
                    EditorInlayItemChunk c = (EditorInlayItemChunk) var4.next();
                    Iterator var6 = c.getCompletionLines().iterator();

                    while (var6.hasNext()) {
                        String line1 = (String) var6.next();
                        ++this.totalLineCount;
                        sb.append(line1).append("\n");
                    }
                }

                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }

                this.content = sb.toString();
                return line;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void addChunk(CompletionRenderType type, List<String> completionLines) {
        if (this.chunks == null) {
            this.chunks = new ArrayList();
        }

        this.chunks.add(new EditorInlayItemChunk(type, completionLines));
        this.totalLineCount += completionLines.size();
    }

    public String getLines(int lineCount) {
        String[] lines = this.content.split("\r\n|\n");
        return lineCount > 0 && lineCount <= lines.length ? (String) Arrays.stream(lines).limit((long) lineCount).reduce("", (s1, s2) -> {
            return s1 + s2 + "\n";
        }) : null;
    }


    @Generated
    public String getRequestId() {
        return this.requestId;
    }

    @Generated
    public String getCacheId() {
        return this.cacheId;
    }

    @Generated
    public int getEditorOffset() {
        return this.editorOffset;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public List<EditorInlayItemChunk> getChunks() {
        return this.chunks;
    }

    @Generated
    public int getTotalLineCount() {
        return this.totalLineCount;
    }

    @Generated
    public boolean isRendered() {
        return this.rendered;
    }

    @Generated
    public long getFirstDisplayTimeMs() {
        return this.firstDisplayTimeMs;
    }

    @Generated
    public long getDisplayTimeMs() {
        return this.displayTimeMs;
    }

    @Generated
    public int getRankIndex() {
        return this.rankIndex;
    }

    @Generated
    public String getBatchId() {
        return this.batchId;
    }

    @Generated
    public boolean isAccepted() {
        return this.accepted;
    }

    @Generated
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Generated
    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    @Generated
    public void setEditorOffset(int editorOffset) {
        this.editorOffset = editorOffset;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setChunks(List<EditorInlayItemChunk> chunks) {
        this.chunks = chunks;
    }

    @Generated
    public void setTotalLineCount(int totalLineCount) {
        this.totalLineCount = totalLineCount;
    }

    @Generated
    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    @Generated
    public void setFirstDisplayTimeMs(long firstDisplayTimeMs) {
        this.firstDisplayTimeMs = firstDisplayTimeMs;
    }

    @Generated
    public void setDisplayTimeMs(long displayTimeMs) {
        this.displayTimeMs = displayTimeMs;
    }

    @Generated
    public void setRankIndex(int rankIndex) {
        this.rankIndex = rankIndex;
    }

    @Generated
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    @Generated
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CodeEditorInlayItem)) {
            return false;
        } else {
            CodeEditorInlayItem other = (CodeEditorInlayItem) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label95:
                {
                    Object this$requestId = this.getRequestId();
                    Object other$requestId = other.getRequestId();
                    if (this$requestId == null) {
                        if (other$requestId == null) {
                            break label95;
                        }
                    } else if (this$requestId.equals(other$requestId)) {
                        break label95;
                    }

                    return false;
                }

                Object this$cacheId = this.getCacheId();
                Object other$cacheId = other.getCacheId();
                if (this$cacheId == null) {
                    if (other$cacheId != null) {
                        return false;
                    }
                } else if (!this$cacheId.equals(other$cacheId)) {
                    return false;
                }

                if (this.getEditorOffset() != other.getEditorOffset()) {
                    return false;
                } else {
                    Object this$content = this.getContent();
                    Object other$content = other.getContent();
                    if (this$content == null) {
                        if (other$content != null) {
                            return false;
                        }
                    } else if (!this$content.equals(other$content)) {
                        return false;
                    }

                    label73:
                    {
                        Object this$chunks = this.getChunks();
                        Object other$chunks = other.getChunks();
                        if (this$chunks == null) {
                            if (other$chunks == null) {
                                break label73;
                            }
                        } else if (this$chunks.equals(other$chunks)) {
                            break label73;
                        }

                        return false;
                    }

                    if (this.getTotalLineCount() != other.getTotalLineCount()) {
                        return false;
                    } else if (this.isRendered() != other.isRendered()) {
                        return false;
                    } else if (this.getFirstDisplayTimeMs() != other.getFirstDisplayTimeMs()) {
                        return false;
                    } else if (this.getDisplayTimeMs() != other.getDisplayTimeMs()) {
                        return false;
                    } else if (this.getRankIndex() != other.getRankIndex()) {
                        return false;
                    } else {
                        Object this$batchId = this.getBatchId();
                        Object other$batchId = other.getBatchId();
                        if (this$batchId == null) {
                            if (other$batchId != null) {
                                return false;
                            }
                        } else if (!this$batchId.equals(other$batchId)) {
                            return false;
                        }

                        return true;
                    }
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CodeEditorInlayItem;
    }

    @Generated
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $requestId = this.getRequestId();
        result = result * 59 + ($requestId == null ? 43 : $requestId.hashCode());
        Object $cacheId = this.getCacheId();
        result = result * 59 + ($cacheId == null ? 43 : $cacheId.hashCode());
        result = result * 59 + this.getEditorOffset();
        Object $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        Object $chunks = this.getChunks();
        result = result * 59 + ($chunks == null ? 43 : $chunks.hashCode());
        result = result * 59 + this.getTotalLineCount();
        result = result * 59 + (this.isRendered() ? 79 : 97);
        long $firstDisplayTimeMs = this.getFirstDisplayTimeMs();
        result = result * 59 + (int) ($firstDisplayTimeMs >>> 32 ^ $firstDisplayTimeMs);
        long $displayTimeMs = this.getDisplayTimeMs();
        result = result * 59 + (int) ($displayTimeMs >>> 32 ^ $displayTimeMs);
        result = result * 59 + this.getRankIndex();
        Object $batchId = this.getBatchId();
        result = result * 59 + ($batchId == null ? 43 : $batchId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getRequestId();
        return "CodeEditorInlayItem(requestId=" + var10000 + ", cacheId=" + this.getCacheId() + ", editorOffset=" + this.getEditorOffset() + ", content=" + this.getContent() + ", chunks=" + this.getChunks() + ", totalLineCount=" + this.getTotalLineCount() + ", rendered=" + this.isRendered() + ", firstDisplayTimeMs=" + this.getFirstDisplayTimeMs() + ", displayTimeMs=" + this.getDisplayTimeMs() + ", rankIndex=" + this.getRankIndex() + ", batchId=" + this.getBatchId() + ")";
    }

    @Generated
    public CodeEditorInlayItem() {
    }

    @Generated
    public CodeEditorInlayItem(String requestId, String cacheId, int editorOffset, String content,
                               List<EditorInlayItemChunk> chunks, int totalLineCount, boolean rendered,
                               long firstDisplayTimeMs, long displayTimeMs, int rankIndex, String batchId
            , boolean accepted) {
        this.requestId = requestId;
        this.cacheId = cacheId;
        this.editorOffset = editorOffset;
        this.content = content;
        this.chunks = chunks;
        this.totalLineCount = totalLineCount;
        this.rendered = rendered;
        this.firstDisplayTimeMs = firstDisplayTimeMs;
        this.displayTimeMs = displayTimeMs;
        this.rankIndex = rankIndex;
        this.batchId = batchId;
        this.accepted = accepted;
    }

    public static class EditorInlayItemChunk {
        private CompletionRenderType type;
        private List<String> completionLines;

        @Generated
        public CompletionRenderType getType() {
            return this.type;
        }

        @Generated
        public List<String> getCompletionLines() {
            return this.completionLines;
        }

        @Generated
        public void setType(CompletionRenderType type) {
            this.type = type;
        }

        @Generated
        public void setCompletionLines(List<String> completionLines) {
            this.completionLines = completionLines;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof EditorInlayItemChunk)) {
                return false;
            } else {
                EditorInlayItemChunk other = (EditorInlayItemChunk) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$type = this.getType();
                    Object other$type = other.getType();
                    if (this$type == null) {
                        if (other$type != null) {
                            return false;
                        }
                    } else if (!this$type.equals(other$type)) {
                        return false;
                    }

                    Object this$completionLines = this.getCompletionLines();
                    Object other$completionLines = other.getCompletionLines();
                    if (this$completionLines == null) {
                        if (other$completionLines != null) {
                            return false;
                        }
                    } else if (!this$completionLines.equals(other$completionLines)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof EditorInlayItemChunk;
        }

        @Generated
        public int hashCode() {
            //int PRIME = true;
            int result = 1;
            Object $type = this.getType();
            result = result * 59 + ($type == null ? 43 : $type.hashCode());
            Object $completionLines = this.getCompletionLines();
            result = result * 59 + ($completionLines == null ? 43 : $completionLines.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            CompletionRenderType var10000 = this.getType();
            return "CodeEditorInlayItem.EditorInlayItemChunk(type=" + var10000 + ", completionLines=" + this.getCompletionLines() + ")";
        }

        @Generated
        public EditorInlayItemChunk() {
        }

        @Generated
        public EditorInlayItemChunk(CompletionRenderType type, List<String> completionLines) {
            this.type = type;
            this.completionLines = completionLines;
        }
    }
}
