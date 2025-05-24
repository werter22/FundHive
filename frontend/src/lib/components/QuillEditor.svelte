<script>
  import { onMount, createEventDispatcher } from "svelte";
  import Quill from "quill";
  import BlotFormatter from "quill-blot-formatter";
  import "quill/dist/quill.snow.css";

  export let content = "";

  Quill.register("modules/blotFormatter", BlotFormatter);

  const dispatch = createEventDispatcher();
  let editorDiv;
  let quill;

  // Replace with your actual Cloudinary info
  const cloudName = "dksmrobtx";
  const uploadPreset = "fundhive_uploads";

  function imageHandler() {
    const input = document.createElement("input");
    input.setAttribute("type", "file");
    input.setAttribute("accept", "image/*");
    input.click();

    input.onchange = async () => {
      const file = input.files?.[0];
      if (!file) return;

      const formData = new FormData();
      formData.append("file", file);
      formData.append("upload_preset", uploadPreset);
      formData.append("folder", "startups");

      const res = await fetch(
        `https://api.cloudinary.com/v1_1/${cloudName}/image/upload`,
        {
          method: "POST",
          body: formData,
        },
      );

      const data = await res.json();
      const range = quill.getSelection(true);
      quill.insertEmbed(range.index, "image", data.secure_url);
    };
  }

  const modules = {
    toolbar: {
      container: [
        [{ header: [1, 2, false] }],
        ["bold", "italic", "underline"],
        ["link", "image"],
        [{ list: "ordered" }, { list: "bullet" }],
        ["clean"],
      ],
      handlers: {
        image: imageHandler,
      },
    },
    blotFormatter: {},
  };

  export function getHtml() {
    return quill?.root.innerHTML || "";
  }

  onMount(() => {
    quill = new Quill(editorDiv, {
      theme: "snow",
      modules,
    });

    quill.root.innerHTML = content;

    quill.on("text-change", () => {
      dispatch("input", quill.root.innerHTML);
    });
  });

  $: if (quill && quill.root.innerHTML !== content) {
    const sel = quill.getSelection();
    quill.root.innerHTML = content;
    if (sel) quill.setSelection(sel);
  }
</script>

<div bind:this={editorDiv}></div>

<style>
:global(.ql-toolbar) {
  color: white;
    background: rgba(61, 61, 61, 0.2);
    border-radius: 6px;
  }

  :global(.ql-editor) {
    color: white;
    background: rgba(61, 61, 61, 0.2);
    min-height: 200px;
  }

  :global(.ql-editor img) {
    max-width: 100%;
    height: auto;
    display: block;
    margin: 1rem auto;
    border-radius: 6px;
  }  
  
</style>
