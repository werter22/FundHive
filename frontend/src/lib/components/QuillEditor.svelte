<script>
  import { onMount, createEventDispatcher } from "svelte";
  import Quill from "quill";
  import "quill/dist/quill.snow.css";

  // the HTML content to edit
  export let content = "";
  // Quill modules (e.g. toolbar configuration)
  export let modules = {
    toolbar: [
      [{ header: [1, 2, false] }],
      ["bold", "italic", "underline"],
      ["link", "image"],
      [{ list: "ordered" }, { list: "bullet" }],
      ["clean"]
    ]
  };

  export function getHtml() {
  return quill?.root.innerHTML || "";
}

  const dispatch = createEventDispatcher();
  let editorDiv;
  let quill;

  onMount(() => {
    quill = new Quill(editorDiv, {
      theme: "snow",
      modules
    });
    // initialize editor HTML
    quill.root.innerHTML = content;
    // emit changes upstream
    quill.on("text-change", () => {
      dispatch("input", quill.root.innerHTML);
    });
  });

  // reactive update: if parent changes content, update editor
  $: if (quill && quill.root.innerHTML !== content) {
    const sel = quill.getSelection();
    quill.root.innerHTML = content;
    if (sel) quill.setSelection(sel);
  }
</script>

<style>
  :global(.ql-editor) {
    min-height: 200px;
  }
</style>

<div bind:this={editorDiv}></div>
