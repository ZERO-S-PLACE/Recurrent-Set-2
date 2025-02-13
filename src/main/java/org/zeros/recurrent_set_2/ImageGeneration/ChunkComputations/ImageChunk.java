package org.zeros.recurrent_set_2.ImageGeneration.ChunkComputations;

import lombok.Builder;

@Builder
public record ImageChunk(int rowsStart, int rowsEnd, int columnsStart, int columnsEnd) {
}
