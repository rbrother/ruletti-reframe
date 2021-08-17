(defproject verto-gui "1.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [re-frame "1.2.0"]
                 [garden "1.3.10"]
                 [net.dhleong/spade "1.1.0"]])
