package lasta.opennlp.character.extractor.training

import com.atilika.kuromoji.TokenizerBase
import com.atilika.kuromoji.ipadic.Token
import com.atilika.kuromoji.ipadic.Tokenizer
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import kotlin.system.exitProcess

object ModelGenerator {
    private val TOKENIZER: Tokenizer = Tokenizer.Builder()
            .mode(TokenizerBase.Mode.NORMAL)
            .userDictionary(TrainingResource.DICTIONARY)
            .build()

    @JvmStatic
    fun main(args: Array<String>) {
        val sentences: List<String> = try {
            InputStreamReader(TrainingResource.RESTAURANT).use { it.readLines() }
        } catch (e: IOException) {
            e.printStackTrace()
            exitProcess(1)
        }

        val sentencesWithLabels: List<String> = sentences.map { sentence -> sentence.putLabels() }

        sentencesWithLabels.toFile(TrainingResource.TRAINING_DATA_PATH)
    }

    private fun List<String>.toFile(path: String) {
        File(path).printWriter().use { writer ->
            this.forEach { line ->
                println(line)
                writer.println(line)
                writer.println()
            }
        }
    }

    private fun String.putLabels(): String = TOKENIZER.tokenize(this)
            .joinToString(" ") { token ->
                when (token.partOfSpeechLevel1) {
                    "Character" -> token.toStrWithLabel()
                    else -> token.surface
                }
            }

    private fun Token.toStrWithLabel(): String {
        val emptySymbol = "*"
        val sb = StringBuilder()
        sb.append("<START:")
        sb.append(this.partOfSpeechLevel1)
        if (this.partOfSpeechLevel2 != emptySymbol) sb.append("-").append(partOfSpeechLevel2)
        if (this.partOfSpeechLevel3 != emptySymbol) sb.append("-").append(partOfSpeechLevel3)
        if (this.partOfSpeechLevel4 != emptySymbol) sb.append("-").append(partOfSpeechLevel4)
        sb.append("> ")
        sb.append(this.surface)
        sb.append(" <END>")
        return sb.toString()
    }
}
