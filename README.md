## 插件的作用
用来替代Maven默认的clean插件

## 插件的使用
1. 引入插件后，默认会清理项目中target目录
2. 如果需要删除其他目录的文件，可以通过类似于下面的配置来实现
```xml
    <build>

        <plugins>
            <!-- 覆盖默认的 maven-clean-plugin，禁止其执行 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-clean-plugin</artifactId>
                <version>${maven.clean.version}</version> <!-- 使用与项目中相同的版本 -->
                <executions>
                    <execution>
                        <id>default-clean</id>
                        <phase>none</phase>
                    </execution>
                </executions>
            </plugin>

            <!-- 添加自定义插件 -->
            <plugin>
                <groupId>io.github.lzmien</groupId>
                <artifactId>lzm-clean-plugin</artifactId>
                <version>${power-clean.version}</version>
                <executions>
                    <execution>
                        <id>lzm-clean-plugin-execution</id>
                        <phase>clean</phase> <!-- 这里是maven插件在哪个阶段被调用 -->
                        <goals>
                            <goal>power-clean</goal> <!-- 这里是我们自定义maven插件的名字 -->
                        </goals>
                    </execution>
                </executions>
                <!--配置自定义插件-->
                <configuration>
                    <fileSettings>
                        <fileSetting>
                            <dir>src/main/java/com/lizemin/temp</dir>
                            <includes>
                                <include>**.html</include>
                                <include>**.txt</include>
                            </includes>
                        </fileSetting>
                    </fileSettings>
                </configuration>
            </plugin>

        </plugins>
    </build>
```
3. 在上面的配置中，我们通过<fileSettings>标签来配置需要额外删除的临时目录，如果只是想删除目录中的部分文件，可以通过<include>标签实现



