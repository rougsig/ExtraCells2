package extracells.core

interface Provider<T> {
  fun get(): T
}

inline fun <T> provider(crossinline block: () -> T): Provider<T> {
  return object : Provider<T> {
    override fun get(): T {
      return block()
    }
  }
}