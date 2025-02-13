package org.zeros.recurrent_set_2.ImageGeneration.ChunkComputations;

import lombok.Builder;

@Builder
public record ImageChunkBorders(int[] leftBorder, int[] rightBorder, int[] topBorder, int[] bottomBorder) {

}
