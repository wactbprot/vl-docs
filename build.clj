(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'com.github.wactbprot/doc)
(def version (format "0.2.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn" :aliases [:dev]}))
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn prep [_]
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["bases/srv/src"]})
  (b/copy-dir {:src-dirs ["bases/srv/src"
                          "bases/srv/resources"
                          "components/config/src"
                          "components/content/src"
                          "components/db/src"
                          "components/page/src"]
               :target-dir class-dir}))

(defn uber [_]
  (b/compile-clj {:basis basis
                  :src-dirs ["bases/src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :main 'wactbprot.doc.srv.core
           :uber-file uber-file
           :basis basis}))
