import io.github.sgpublic.exsp.annotations.ExSharedPreference
import org.junit.Test

class AnnotationNameTest {
    @Test
    fun println() {
        println(ExSharedPreference::class.java.canonicalName)
    }
}
