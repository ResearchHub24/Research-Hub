
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken


-keepattributes AnnotationDefault,RuntimeVisibleAnnotations

-keepattributes Signature
-keepclassmembers class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.atech.core.model.** { *; }
-keep class com.atech.core.utils.JsonKt { *; }

-keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

 -keep class * extends androidx.room.RoomDatabase
 -keep @androidx.room.Entity class *