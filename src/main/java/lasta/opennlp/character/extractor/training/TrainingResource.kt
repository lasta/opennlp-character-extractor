package lasta.opennlp.character.extractor.training

import java.io.InputStream

object TrainingResource {
    val DICTIONARY: InputStream by lazy { ClassLoader.getSystemResourceAsStream("characters.dict.txt") }
    // https://www.aozora.gr.jp/cards/000081/files/43754_17659.html
    val RESTAURANT: InputStream by lazy { ClassLoader.getSystemResourceAsStream("kenji-miyazawa/restaurant-with-a-lot-of-orders.txt") }
    const val TRAINING_DATA_PATH: String = "character-finder.train"
}