package com.tooneCode.editor.model;

import com.tooneCode.completion.model.CompletionRenderType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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


    public String getRequestId() {
        return this.requestId;
    }


    public String getCacheId() {
        return this.cacheId;
    }


    public int getEditorOffset() {
        return this.editorOffset;
    }


    public String getContent() {
        return this.content;
    }


    public List<EditorInlayItemChunk> getChunks() {
        return this.chunks;
    }


    public int getTotalLineCount() {
        return this.totalLineCount;
    }


    public boolean isRendered() {
        return this.rendered;
    }


    public long getFirstDisplayTimeMs() {
        return this.firstDisplayTimeMs;
    }


    public long getDisplayTimeMs() {
        return this.displayTimeMs;
    }


    public int getRankIndex() {
        return this.rankIndex;
    }


    public String getBatchId() {
        return this.batchId;
    }


    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }


    public void setEditorOffset(int editorOffset) {
        this.editorOffset = editorOffset;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public void setChunks(List<EditorInlayItemChunk> chunks) {
        this.chunks = chunks;
    }


    public void setTotalLineCount(int totalLineCount) {
        this.totalLineCount = totalLineCount;
    }


    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }


    public void setFirstDisplayTimeMs(long firstDisplayTimeMs) {
        this.firstDisplayTimeMs = firstDisplayTimeMs;
    }


    public void setDisplayTimeMs(long displayTimeMs) {
        this.displayTimeMs = displayTimeMs;
    }


    public void setRankIndex(int rankIndex) {
        this.rankIndex = rankIndex;
    }


    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }


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


    protected boolean canEqual(Object other) {
        return other instanceof CodeEditorInlayItem;
    }


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


    public String toString() {
        String var10000 = this.getRequestId();
        return "CodeEditorInlayItem(requestId=" + var10000 + ", cacheId=" + this.getCacheId() + ", editorOffset=" + this.getEditorOffset() + ", content=" + this.getContent() + ", chunks=" + this.getChunks() + ", totalLineCount=" + this.getTotalLineCount() + ", rendered=" + this.isRendered() + ", firstDisplayTimeMs=" + this.getFirstDisplayTimeMs() + ", displayTimeMs=" + this.getDisplayTimeMs() + ", rankIndex=" + this.getRankIndex() + ", batchId=" + this.getBatchId() + ")";
    }


    public CodeEditorInlayItem() {
    }


    public CodeEditorInlayItem(String requestId, String cacheId, int editorOffset, String content, List<EditorInlayItemChunk> chunks, int totalLineCount, boolean rendered, long firstDisplayTimeMs, long displayTimeMs, int rankIndex, String batchId) {
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
    }

    public static class EditorInlayItemChunk {
        private CompletionRenderType type;
        private List<String> completionLines;


        public CompletionRenderType getType() {
            return this.type;
        }


        public List<String> getCompletionLines() {
            return this.completionLines;
        }


        public void setType(CompletionRenderType type) {
            this.type = type;
        }


        public void setCompletionLines(List<String> completionLines) {
            this.completionLines = completionLines;
        }


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


        protected boolean canEqual(Object other) {
            return other instanceof EditorInlayItemChunk;
        }


        public int hashCode() {
            //int PRIME = true;
            int result = 1;
            Object $type = this.getType();
            result = result * 59 + ($type == null ? 43 : $type.hashCode());
            Object $completionLines = this.getCompletionLines();
            result = result * 59 + ($completionLines == null ? 43 : $completionLines.hashCode());
            return result;
        }


        public String toString() {
            CompletionRenderType var10000 = this.getType();
            return "CodeEditorInlayItem.EditorInlayItemChunk(type=" + var10000 + ", completionLines=" + this.getCompletionLines() + ")";
        }


        public EditorInlayItemChunk() {
        }


        public EditorInlayItemChunk(CompletionRenderType type, List<String> completionLines) {
            this.type = type;
            this.completionLines = completionLines;
        }
    }
}
